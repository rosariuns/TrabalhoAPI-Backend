package com.example.api.controller;

import com.example.api.model.Livro;
import com.example.api.repository.LivroRepository;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/livros")
public class LivroController {

    private final LivroRepository repository;

    public LivroController(LivroRepository repository) {
        this.repository = repository;
    }

    // GET /livros?autor=...&genero=...&disponivel=...&anoMinimo=...
    @GetMapping
    public ResponseEntity<List<Livro>> listar(
            @RequestParam(required = false) String autor,
            @RequestParam(required = false) String genero,
            @RequestParam(required = false) Boolean disponivel,
            @RequestParam(required = false) Integer anoMinimo) {

        if (autor == null && genero == null && disponivel == null && anoMinimo == null) {
            return ResponseEntity.ok(repository.findAll(Sort.by("id")));
        }
      
        String autorBusca = (autor == null) ? "" : autor;
        String generoBusca = (genero == null) ? "" : genero;
        boolean filtrarDisponivel = (disponivel != null);
        boolean valorDisponivel = (disponivel != null) && disponivel;
        int ano = (anoMinimo == null) ? 0 : anoMinimo;

        return ResponseEntity.ok(
                repository.filtrar(autorBusca, generoBusca, filtrarDisponivel, valorDisponivel, ano));
    }

    // GET /livros/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Livro> buscarPorId(@PathVariable Long id) {
        Optional<Livro> livro = repository.findById(id);
        if (livro.isPresent()) {
            return ResponseEntity.ok(livro.get());
        }
        return ResponseEntity.notFound().build(); // 404
    }

    // POST /livros
    @PostMapping
    public ResponseEntity<Livro> cadastrar(@RequestBody Livro novoLivro) {
        novoLivro.setId(null);
        Livro salvo = repository.save(novoLivro);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo); // 201
    }

    // PUT /livros/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Livro> atualizar(@PathVariable Long id,
                                           @RequestBody Livro dadosNovos) {
        Optional<Livro> existente = repository.findById(id);
        if (existente.isEmpty()) {
            return ResponseEntity.notFound().build(); // 404
        }
        Livro livro = existente.get();
        livro.setTitulo(dadosNovos.getTitulo());
        livro.setAutor(dadosNovos.getAutor());
        livro.setGenero(dadosNovos.getGenero());
        livro.setAnoPublicacao(dadosNovos.getAnoPublicacao());
        livro.setDisponivel(dadosNovos.getDisponivel());
        return ResponseEntity.ok(repository.save(livro));
    }

    // DELETE /livros/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        Optional<Livro> livro = repository.findById(id);
        if (livro.isEmpty()) {
            return ResponseEntity.notFound().build(); // 404
        }
        repository.delete(livro.get());
        return ResponseEntity.noContent().build(); // 204
    }
}