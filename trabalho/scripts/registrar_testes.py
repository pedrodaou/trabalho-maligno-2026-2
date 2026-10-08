#!/usr/bin/env python3
"""Conserva o build real e identifica os fontes verificados por SHA-256."""
import argparse
import hashlib
from pathlib import Path
import xml.etree.ElementTree as ET

ROOT = Path(__file__).resolve().parents[1]


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("build_log", type=Path)
    args = parser.parse_args()
    log = args.build_log.read_text(encoding="utf-8")
    if "BUILD SUCCESS" not in log:
        raise SystemExit("O build fornecido nao terminou com sucesso.")
    suites = list((ROOT / "target" / "surefire-reports").glob("TEST-*.xml"))
    if not suites:
        raise SystemExit("Nao ha resultados de testes em target/surefire-reports.")
    quantidade = 0
    for arquivo in suites:
        suite = ET.parse(arquivo).getroot()
        if int(suite.attrib["failures"]) or int(suite.attrib["errors"]):
            raise SystemExit("Existem falhas nos testes.")
        quantidade += int(suite.attrib["tests"])
    evidencias = ROOT / "docs" / "evidencias"
    evidencias.mkdir(parents=True, exist_ok=True)
    (evidencias / "testes.txt").write_text(log, encoding="utf-8")
    hashes = [hashlib.sha256(arquivo.read_bytes()).hexdigest() + "  "
              + arquivo.relative_to(ROOT).as_posix()
              for arquivo in sorted((ROOT / "src").rglob("*.java"))]
    (evidencias / "fontes-verificados.sha256").write_text(
        "\n".join(hashes) + "\n", encoding="utf-8")
    print(f"{quantidade} testes sem falhas; build e hashes registrados.")


if __name__ == "__main__":
    main()
