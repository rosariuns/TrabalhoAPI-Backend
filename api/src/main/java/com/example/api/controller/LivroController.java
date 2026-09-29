package com.example.api.controller;

import com.example.api.model.Livro;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/livros")
public class LivroController {

    // "Banco de dados" em memória: os dados somem quando a aplicação reinicia
    private final List<Livro> livros = new ArrayList<>();

    // Gerador manual de ID
    private Long proximoId = 1L;

    // Alguns livros iniciais para facilitar os testes
    public LivroController() {
        livros.add(new Livro(proximoId++, "Dom Casmurro", "Machado de Assis", "Romance", 1899, true));
        livros.add(new Livro(proximoId++, "Memórias Póstumas de Brás Cubas", "Machado de Assis", "Romance", 1881, false));
        livros.add(new Livro(proximoId++, "O Hobbit", "J.R.R. Tolkien", "Fantasia", 1937, true));
        livros.add(new Livro(proximoId++, "1984", "George Orwell", "Ficção", 1949, true));
    }

    // GET /livros
    // Lista todos. Aceita filtros opcionais (podem ser combinados):
    // /livros?autor=machado&genero=romance&disponivel=true&anoMinimo=1890
    @GetMapping
    public ResponseEntity<List<Livro>> listar(
            @RequestParam(required = false) String autor,
            @RequestParam(required = false) String genero,
            @RequestParam(required = false) Boolean disponivel,
            @RequestParam(required = false) Integer anoMinimo) {

        List<Livro> resultado = new ArrayList<>();

        for (Livro livro : livros) {
            boolean ok = true;

            if (autor != null && !livro.getAutor().toLowerCase().contains(autor.toLowerCase())) {
                ok = false;
            }
            if (genero != null && !livro.getGenero().equalsIgnoreCase(genero)) {
                ok = false;
            }
            if (disponivel != null && !livro.getDisponivel().equals(disponivel)) {
                ok = false;
            }
            if (anoMinimo != null && livro.getAnoPublicacao() < anoMinimo) {
                ok = false;
            }

            if (ok) {
                resultado.add(livro);
            }
        }

        return ResponseEntity.ok(resultado);
    }

    // GET /livros/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Livro> buscarPorId(@PathVariable Long id) {
        for (Livro livro : livros) {
            if (livro.getId().equals(id)) {
                return ResponseEntity.ok(livro);
            }
        }
        return ResponseEntity.notFound().build(); // 404
    }

    // POST /livros
    @PostMapping
    public ResponseEntity<Livro> cadastrar(@RequestBody Livro novoLivro) {
        novoLivro.setId(proximoId++); // o ID é gerado pela API, não pelo cliente
        livros.add(novoLivro);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoLivro); // 201
    }

    // PUT /livros/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Livro> atualizar(@PathVariable Long id,
                                           @RequestBody Livro dadosNovos) {
        for (Livro livro : livros) {
            if (livro.getId().equals(id)) {
                livro.setTitulo(dadosNovos.getTitulo());
                livro.setAutor(dadosNovos.getAutor());
                livro.setGenero(dadosNovos.getGenero());
                livro.setAnoPublicacao(dadosNovos.getAnoPublicacao());
                livro.setDisponivel(dadosNovos.getDisponivel());
                return ResponseEntity.ok(livro);
            }
        }
        return ResponseEntity.notFound().build(); // 404
    }

    // DELETE /livros/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        for (Livro livro : livros) {
            if (livro.getId().equals(id)) {
                livros.remove(livro);
                return ResponseEntity.noContent().build(); // 204
            }
        }
        return ResponseEntity.notFound().build(); // 404
    }
}