#!/usr/bin/env python3
"""Cria ZIP com arquivos rastreados, JAR compilado e manifesto SHA-256."""
import hashlib
import subprocess
import zipfile
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]


def main():
    jar = ROOT / "target" / "trabalho-maligno-1.0-SNAPSHOT.jar"
    if not jar.is_file():
        raise SystemExit("Compile com mvn clean test package antes de empacotar.")
    arquivos = subprocess.check_output(["git", "ls-files", "-z"], cwd=ROOT).decode().split("\0")
    itens = [(arquivo, ROOT / arquivo) for arquivo in arquivos if arquivo]
    itens.append(("bin/" + jar.name, jar))
    destino = ROOT / "dist" / "trabalho-maligno-2026-2.zip"
    destino.parent.mkdir(exist_ok=True)
    manifestos = []
    with zipfile.ZipFile(destino, "w", zipfile.ZIP_DEFLATED) as pacote:
        for nome, caminho in itens:
            dados = caminho.read_bytes()
            pacote.writestr(nome, dados)
            manifestos.append(hashlib.sha256(dados).hexdigest() + "  " + nome)
        pacote.writestr("SHA256SUMS.txt", "\n".join(manifestos) + "\n")
    print(destino)
    print("SHA-256: " + hashlib.sha256(destino.read_bytes()).hexdigest())


if __name__ == "__main__":
    main()
