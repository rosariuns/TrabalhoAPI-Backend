package com.example.api.repository;

import com.example.api.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LivroRepository extends JpaRepository<Livro, Long> {

    @Query("SELECT l FROM Livro l WHERE " +
           "LOWER(l.autor) LIKE LOWER(CONCAT('%', :autor, '%')) " +
           "AND (:genero = '' OR LOWER(l.genero) = LOWER(:genero)) " +
           "AND (:filtrarDisponivel = false OR l.disponivel = :disponivel) " +
           "AND l.anoPublicacao >= :anoMinimo " +
           "ORDER BY l.id")
    List<Livro> filtrar(@Param("autor") String autor,
                        @Param("genero") String genero,
                        @Param("filtrarDisponivel") boolean filtrarDisponivel,
                        @Param("disponivel") boolean disponivel,
                        @Param("anoMinimo") int anoMinimo);
}