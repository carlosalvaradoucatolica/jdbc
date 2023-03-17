package co.edu.ucatolica.jdbc;

import co.edu.ucatolica.jdbc.util.Response;
import co.edu.ucatolica.jdbc.util.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class Controller {

    @Autowired
    private JdbcTemplate jdbc;

    @GetMapping("/list")
    public ResponseEntity<Response> getUsuarios() {
        List<User> usuarios = jdbc.query(
          "SELECT * FROM user",
                new BeanPropertyRowMapper<>(User.class)
        );

        return  new ResponseEntity<Response>(
                Response.builder()
                        .timeStampo(LocalDateTime.now())
                        .message("Data retrieved succesful.")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .data(Map.of("usuarios",usuarios))
                        .build()
                , HttpStatus.OK
        );
    }

    @GetMapping("get/{id}")
    public ResponseEntity<Response> getUsuarioById(@PathVariable Integer id) {
        try {
            User usuario = jdbc.queryForObject(
                    "SELECT * FROM user WHERE id = ?",
                    new Object[]{id},
                    new BeanPropertyRowMapper<>(User.class)
            );

            return  new ResponseEntity<Response>(
                    Response.builder()
                            .timeStampo(LocalDateTime.now())
                            .message("Data retrieved succesful.")
                            .status(HttpStatus.OK)
                            .statusCode(HttpStatus.OK.value())
                            .data(Map.of("usuario",usuario))
                            .build()
                    , HttpStatus.OK
            );
        } catch (EmptyResultDataAccessException e){
            return  new ResponseEntity<Response>(
                    Response.builder()
                            .timeStampo(LocalDateTime.now())
                            .message("User id not found.")
                            .status(HttpStatus.NOT_FOUND)
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .build()
                    , HttpStatus.NOT_FOUND
            );
        }
    }

    @PostMapping("/")
    public ResponseEntity<Response> createUsuario(@RequestBody User usuario) {
        int persisted = jdbc.update(
                "INSERT INTO user(username, password) VALUES(?, ?)",
                usuario.getUsername(), usuario.getPassword()
        );
        if (persisted == 1) {
            return  new ResponseEntity<Response>(
                    Response.builder()
                            .timeStampo(LocalDateTime.now())
                            .message("Data persited succesful.")
                            .status(HttpStatus.CREATED)
                            .statusCode(HttpStatus.CREATED.value())
                            .build()
                    , HttpStatus.CREATED
            );
        } else {
            return  new ResponseEntity<Response>(
                    Response.builder()
                            .timeStampo(LocalDateTime.now())
                            .message("Data incorrect format.")
                            .status(HttpStatus.BAD_REQUEST)
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .build()
                    , HttpStatus.BAD_REQUEST
            );
        }
    }

    @PutMapping("/")
    public ResponseEntity<Response> updateUsuario(@RequestBody User usuario) {
        int result = jdbc.update(
                "UPDATE user SET username = ?, password = ? WHERE id = ?",
                usuario.getUsername(), usuario.getPassword(), usuario.getId());
        if (result == 1) {
            return  new ResponseEntity<Response>(
                    Response.builder()
                            .timeStampo(LocalDateTime.now())
                            .message("Data persited succesful.")
                            .status(HttpStatus.ACCEPTED)
                            .statusCode(HttpStatus.ACCEPTED.value())
                            .build()
                    , HttpStatus.ACCEPTED
            );
        } else {
            return  new ResponseEntity<Response>(
                    Response.builder()
                            .timeStampo(LocalDateTime.now())
                            .message("Data incorrect format.")
                            .status(HttpStatus.BAD_REQUEST)
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .build()
                    , HttpStatus.BAD_REQUEST
            );
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteUsuario(@PathVariable Integer id) {
        int result = jdbc.update(
                "DELETE FROM user WHERE id = ?",
                id);
        if (result == 1) {
            return  new ResponseEntity<Response>(
                    Response.builder()
                            .timeStampo(LocalDateTime.now())
                            .message("Data persited succesful.")
                            .status(HttpStatus.ACCEPTED)
                            .statusCode(HttpStatus.ACCEPTED.value())
                            .build()
                    , HttpStatus.ACCEPTED
            );
        } else {
            return  new ResponseEntity<Response>(
                    Response.builder()
                            .timeStampo(LocalDateTime.now())
                            .message("Data incorrect format.")
                            .status(HttpStatus.BAD_REQUEST)
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .build()
                    , HttpStatus.BAD_REQUEST
            );
        }
    }


}
