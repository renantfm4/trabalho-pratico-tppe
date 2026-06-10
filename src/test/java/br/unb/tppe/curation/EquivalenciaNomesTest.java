package br.unb.tppe.curation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@Tag("Equivalencia")
public class EquivalenciaNomesTest {

    @ParameterizedTest
    @CsvSource(delimiter = ';', value = {
            // Caso 1: Variantes Tipograficas e Acento
            "Monica Hirata Sant`anna; Mônica Hirata Sant’anna; Mônica Hirata Sant'anna",
            "Sergio Henrique Guaraldi; Sérgio Henrique Guaraldi; Sérgio Henrique Guaraldi",
            // Caso 2 & 3: Particulas de / Pontuacao / Abreviacao Simples
            "Ana de Mattos Seabra; Seabra A. M.; Ana de Mattos Seabra",
            "Cassius de Souza; Souza C.; Cassius de Souza",
            "Luiz de Oliveira de Souza; Luiz de O. de Souza; Luiz de Oliveira de Souza",
            "Luiz de Oliveira de Souza; Luiz Oliveira Souza; Luiz de Oliveira de Souza",
            // Caso 4: Iniciais agrupadas
            "Vanilda Cristina Junior; VC Junior; Vanilda Cristina Junior",
            "Sérgio Henrique Guaraldi; SH Guaraldi; Sérgio Henrique Guaraldi"
    })
    public void testDeduplicacaoCasos1A4(String n1, String n2, String esperado) {
        CuradorDados curador = new CuradorDados();
        List<RegistroAutor> autores = Arrays.asList(
                new RegistroAutor(1, n1),
                new RegistroAutor(2, n2)
        );

        List<RegistroAutor> resultado = curador.processar(autores);

        assertEquals(2, resultado.size());
        assertEquals(esperado, resultado.get(0).getNome());
        assertEquals(esperado, resultado.get(1).getNome());
    }
}