package br.unb.tppe.curation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Caso5Id")
public class DeduplicacaoIdTest {

    @Test
    public void testUnificacaoIdsPeloMenorValor() {
        CuradorDados curador = new CuradorDados();
        List<RegistroAutor> autores = Arrays.asList(
                new RegistroAutor(433094, "Raphael Gonçalves Viana"),
                new RegistroAutor(31298, "Raphael Goncalves Viana"),
                new RegistroAutor(549243, "Raphael Gonçalves Viana"),
                new RegistroAutor(608297, "Raphael Gonçalves Viana")
        );

        List<RegistroAutor> resultado = curador.processar(autores);

        assertEquals(4, resultado.size());
        for (RegistroAutor r : resultado) {
            assertEquals(31298, r.getId());
            assertEquals("Raphael Gonçalves Viana", r.getNome());
        }
    }

    @Test
    public void testUnificacaoIdsPeloMenorValorYuri() {
        CuradorDados curador = new CuradorDados();
        List<RegistroAutor> autores = Arrays.asList(
                new RegistroAutor(713897, "Yuri Vieira Faria"),
                new RegistroAutor(500000, "Yuri Vieira Faria")
        );

        List<RegistroAutor> resultado = curador.processar(autores);

        assertEquals(2, resultado.size());
        for (RegistroAutor r : resultado) {
            assertEquals(500000, r.getId());
            assertEquals("Yuri Vieira Faria", r.getNome());
        }
    }
}