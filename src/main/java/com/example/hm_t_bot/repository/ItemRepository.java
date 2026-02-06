package com.example.hm_t_bot.repository;

import com.example.hm_t_bot.model.dto.ItemDto;


import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {
    /*
    private Long id;
    private Long sort_num;
    private String name;
    private LocalDateTime create_date;
    private Integer status;
    */
    private final JdbcTemplate jdbcTemplate;


    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public List<ItemDto> getAllItem() {
        return jdbcTemplate.query("""
                    select row_number() OVER (ORDER BY create_date asc) as rowNum
                        ,id
                        ,name
                    from item
                    where status = 1
                    order by create_date asc
                """, (rs, i) ->
                new ItemDto(
                        rs.getLong("rowNum"),
                        rs.getLong("id"),
                        rs.getString("name")
                )
        );
    }

    public int delItemById(List<Long> ids) {
        String placeholders = String.join(",", Collections.nCopies(ids.size(), "?"));
        String sql = "delete from item where id in (" + placeholders + ")";
        return jdbcTemplate.update(sql, ids.toArray());
    }

    @Transactional
    public void saveItems(List<String> items) {
        jdbcTemplate.batchUpdate("INSERT INTO item (name, create_date, status) " +
                        "VALUES (?, ?, 1)",
                items,
                items.size(),
                (PreparedStatement ps, String item) -> {
                    ps.setString(1, item);
                    ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
                }
        );
    }
}


