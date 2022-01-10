package com.example.shortener;

import com.example.shortener.dto.Link;
import org.springframework.data.repository.CrudRepository;

public interface LinksRepository extends CrudRepository<Link, Integer> {
    Link findByCode(String code);
    Link findByLink(String link);
}
