package br.edu.grupo10.app;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TecladoTest {
    @Test
    void repeteLeituraAteReceberInteiroValido() {
        ByteArrayInputStream entrada = new ByteArrayInputStream(
                "texto\n200\n42\n".getBytes(StandardCharsets.UTF_8));

        try (Teclado teclado = new Teclado(entrada)) {
            int valor = teclado.lerInteiro(
                    "Valor: ", numero -> numero >= 0 && numero <= 100, "Valor invalido.");

            assertEquals(42, valor);
        }
    }

    @Test
    void informaQuandoEntradaTerminaAntesDeUmValor() {
        ByteArrayInputStream entrada = new ByteArrayInputStream(new byte[0]);

        try (Teclado teclado = new Teclado(entrada)) {
            assertThrows(IllegalStateException.class, () ->
                    teclado.lerInteiro("Valor: ", numero -> true, "Valor invalido."));
        }
    }
}

