#!/usr/bin/env python3
"""Executa cenarios reais e gera registros e capturas da saida original do console."""
import argparse
import csv
import html
import io
import json
import platform
import shlex
import shutil
import subprocess
import tempfile
from datetime import datetime
from pathlib import Path
from zoneinfo import ZoneInfo


ROOT = Path(__file__).resolve().parents[1]
EVIDENCIAS = ROOT / "docs" / "evidencias"


def executar_java(classe, entrada="", opcoes=(), argumentos=()):
    java = ["java", "-Duser.timezone=America/Sao_Paulo", *opcoes,
            "-cp", "target/classes", "br.edu.grupo10.app." + classe, *argumentos]
    if shutil.which("java"):
        comando = java
    else:
        comando = ["docker", "run", "--rm", "-i", "-v", f"{ROOT}:/app", "-w", "/app",
                   "eclipse-temurin:21-jdk", *java]
    resultado = subprocess.run(comando, input=entrada, text=True, cwd=ROOT,
                               stdout=subprocess.PIPE, stderr=subprocess.STDOUT,
                               timeout=180, check=True)
    return shlex.join(java), resultado.stdout


def registrar(nome, classe, entrada="", opcoes=(), argumentos=()):
    comando, saida = executar_java(classe, entrada, opcoes, argumentos)
    registro = (f"Classe: br.edu.grupo10.app.{classe}\n"
                f"Data: {datetime.now(ZoneInfo('America/Sao_Paulo')).isoformat()}\n"
                f"Comando: {comando}\n"
                f"Entrada fornecida (uma linha por resposta):\n{entrada or '(sem entrada)'}\n"
                f"Saida original combinada (stdout + stderr):\n{saida}")
    (EVIDENCIAS / f"{nome}.txt").write_text(registro, encoding="utf-8")
    pagina = f"""<!doctype html>
<html lang="pt-BR"><meta charset="utf-8"><title>{html.escape(classe)}</title>
<style>
body {{ background:#111827;color:#f1f5f9;margin:36px;font:18px monospace; }}
h1 {{ font:26px sans-serif; }}
p {{ font:18px sans-serif;color:#cbd5e1; }}
pre {{ white-space:pre-wrap;overflow-wrap:anywhere;background:#0b1220;
      border:1px solid #334155;border-radius:8px;padding:24px;line-height:1.4; }}
</style><h1>{html.escape(classe)} — registro de execucao real</h1>
<p>Captura da pagina de evidencia com a saida original do programa.
Nao e uma captura de um terminal interativo.</p>
<pre>{html.escape(registro)}</pre></html>"""
    (EVIDENCIAS / f"{nome}.html").write_text(pagina, encoding="utf-8")
    return saida


def capturar(nome):
    firefox = shutil.which("firefox")
    if not firefox:
        raise RuntimeError("Instale Firefox para gerar capturas com --capturas.")
    # Firefox instalado via Snap pode nao enxergar o /tmp do host.
    (ROOT / "dist").mkdir(exist_ok=True)
    with tempfile.TemporaryDirectory(prefix="firefox-", dir=ROOT / "dist") as perfil:
        subprocess.run([firefox, "--headless", "--no-remote", "--profile", perfil,
                        "--window-size=1600,1800", "--screenshot",
                        str(EVIDENCIAS / f"{nome}.png"),
                        (EVIDENCIAS / f"{nome}.html").as_uri()],
                       stdout=subprocess.PIPE, stderr=subprocess.STDOUT,
                       timeout=60, check=True)
    if not (EVIDENCIAS / f"{nome}.png").is_file():
        raise RuntimeError(f"Captura nao foi criada: {nome}")


def benchmark():
    _, saida = executar_java("Benchmark", opcoes=("-Xmx512m",),
                            argumentos=("2", "5", "10", "100000", "1000000", "10000000"))
    (EVIDENCIAS / "benchmark.csv").write_text(saida, encoding="utf-8")
    linhas = [linha for linha in saida.splitlines() if not linha.startswith("#")]
    amostras = list(csv.DictReader(io.StringIO("\n".join(linhas))))
    medianas = [linha for linha in amostras if linha["tipo"] == "mediana"]
    assert len(medianas) == 3 and len(amostras) == 18, "CSV incompleto"
    metadata = "\n".join(linha for linha in saida.splitlines() if linha.startswith("#"))
    tabela = ["| Elementos | Mediana sequencial (ms) | Mediana paralela (ms) | Speedup |",
              "|---:|---:|---:|---:|"]
    for linha in medianas:
        tabela.append(f"| {int(linha['elementos']):,} | {float(linha['sequencial_ms']):.3f} | "
                      f"{float(linha['paralelo_ms']):.3f} | {float(linha['speedup']):.2f}x |")
    relatorio = "\n".join([
        "# Experimento de desempenho", "",
        "Ambiente e data registrados abaixo; resultados especificos desta maquina.",
        "", "```text", metadata, "```", "",
        "## Metodo", "",
        "Por tamanho: duas rodadas de aquecimento, depois cinco amostras. A ordem sequencial/paralela",
        "e alternada. Entrada aleatoria reproduzivel com seed 10. Cada resultado e conferido contra",
        "`Arrays.sort` fora da medicao. A geracao, clonagem, validacao e impressao ficam fora dos tempos.",
        "A alocacao dos buffers internos e a criacao/join das threads entram nos tempos.",
        "Os logs do algoritmo paralelo ficam desabilitados neste experimento.", "",
        *tabela, "",
        "Speedup = mediana sequencial / mediana paralela. Abaixo de 1x, o paralelo foi mais lento.",
        "Nao ha garantia de aceleracao: criacao de threads, agendamento, GC e largura de banda da",
        "memoria influenciam os tempos. Cinco amostras nao constituem um benchmark estatistico completo.",
        "", "## Reproducao", "", "```bash",
        "java -Xmx512m -cp target/classes br.edu.grupo10.app.Benchmark 2 5 10 100000 1000000 10000000",
        "```", "", "Dados brutos: [benchmark.csv](evidencias/benchmark.csv).", ""
    ])
    (ROOT / "docs" / "DESEMPENHO.md").write_text(relatorio, encoding="utf-8")
    print("\n".join(tabela))


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--capturas", action="store_true")
    args = parser.parse_args()
    EVIDENCIAS.mkdir(parents=True, exist_ok=True)
    entrada = "7\n1\n38\n27\n43\n3\n9\n82\n10\n1\n"
    esperado = "[3, 9, 10, 27, 38, 43, 82]"
    for nome, classe in [("sequencial", "ProgramaSequencial"), ("paralelo", "ProgramaParalelo"),
                         ("comparador", "Comparador")]:
        saida = registrar(nome, classe, entrada)
        assert esperado in saida, f"Resultado manual incorreto: {classe}"
        if classe == "Comparador":
            assert "os dois resultados sao identicos" in saida
        if args.capturas:
            capturar(nome)

    saida = registrar("entrada-invalida-e-intervalo", "ProgramaSequencial",
                      "abc\n0\n3\n9\n1\n-129\n3\n128\n1\n2\n2\n-1\n0\n9\n1\n")
    assert "Entrada invalida." in saida and "[1, 2]" in saida
    for classe in ["ProgramaSequencial", "ProgramaParalelo", "Comparador"]:
        saida = registrar(f"memoria-{classe}", classe, "100000000\n2\n", opcoes=("-Xmx16m",))
        assert "Memoria insuficiente" in saida

    # Exercita o erro quando as copias cabem, mas o buffer interno nao cabe.
    for classe, tamanho in [("ProgramaSequencial", "9000000"), ("ProgramaParalelo", "9000000"),
                            ("Comparador", "3800000")]:
        saida = registrar(f"buffer-{classe}", classe, f"{tamanho}\n2\n", opcoes=("-Xmx16m",))
        assert "Memoria insuficiente" in saida

    for classe in ["ProgramaParalelo", "Comparador"]:
        saida = registrar(f"cpu-unica-{classe}", classe, opcoes=("-XX:ActiveProcessorCount=1",))
        assert "requer pelo menos 2 processadores" in saida

    saida = registrar("maior-vetor", "MaiorVetorAproximado", "", opcoes=("-Xmx32m",))
    assert "Falhou em" in saida and "Maior vetor que coube" in saida
    if args.capturas:
        capturar("maior-vetor")

    benchmark()
    ambiente = {"data": datetime.now(ZoneInfo("America/Sao_Paulo")).isoformat(),
                "host": platform.platform(),
                "execucao": "JDK local" if shutil.which("java") else "Docker eclipse-temurin:21-jdk",
                "cenarios_console": 13,
                "observacao_capturas": "Paginas de evidencia da saida real; nao terminal interativo."}
    (EVIDENCIAS / "ambiente.json").write_text(
        json.dumps(ambiente, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    print("13 cenarios de console validados; benchmark concluido.")


if __name__ == "__main__":
    main()
