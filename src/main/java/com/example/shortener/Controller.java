package com.example.shortener;

import com.example.shortener.dto.FullLinkDto;
import com.example.shortener.dto.Link;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpHeaders.LOCATION;

@RestController
@RequiredArgsConstructor
public class Controller {

    private final LinksRepository linksRepository;
    private static final  String SERVICE_URL = "http://sh.rt/";

    @PostMapping
    public String createLink(@RequestBody FullLinkDto fullLinkDto) {
        Link savedLink = linksRepository.findByLink(fullLinkDto.getUrl());
        if (savedLink == null) {
            String code = RandomStringUtils.randomAlphanumeric(4);
            linksRepository.save(Link.builder()
                            .code(code)
                            .link(fullLinkDto.getUrl())
                    .build());
            return SERVICE_URL + code;
        } else {
            return SERVICE_URL + savedLink.getCode();
        }
    }



    @GetMapping("/{code}")
    public ResponseEntity<String> getFullUrl(@PathVariable String code) {
        Link link = linksRepository.findByCode(code);
        if(link == null) {
            return ResponseEntity.notFound().build();
        } else {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(LOCATION, link.getLink());
            return ResponseEntity.status(HttpStatus.FOUND)
                    .headers(httpHeaders)
                    .build();
        }
    }
}
