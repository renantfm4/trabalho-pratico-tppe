package br.unb.tppe.curation;

import java.util.Objects;

public class RegistroAutor {
    private int id;
    private String nome;

    public RegistroAutor(int id, String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O Nome não pode ser nulo ou vazio.");
        }
        if (id <= 0) {
            throw new IllegalArgumentException("ID deve ser um numero positivo.");
        }
        this.id = id;
        this.nome = nome.trim();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegistroAutor that = (RegistroAutor) o;
        return id == that.id && Objects.equals(nome, that.nome);
    }

    @Override
    public int hashCode() { return Objects.hash(id, nome); }
}