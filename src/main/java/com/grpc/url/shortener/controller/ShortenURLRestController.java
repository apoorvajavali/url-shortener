package com.grpc.url.shortener.controller;

import com.grpc.url.shortener.client.URLShortenerGrpcClient;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class ShortenURLRestController {
    @RequestMapping(value = "/shortenurl", method = RequestMethod.POST)
    public ResponseEntity<Object> getShortenUrl(@RequestBody String full_Url) {
        UrlValidator urlValidator = new UrlValidator(new String[]{"http", "https"});

        if (urlValidator.isValid(full_Url)) {

            URLShortenerGrpcClient grpcClient = new URLShortenerGrpcClient();
            String response = grpcClient.getShortURL(full_Url);

            return new ResponseEntity<Object>(response, HttpStatus.OK);
        } else {
            String msg = "Input URL is invalid. Provide a valid URL to get a shortened URL.";
            return new ResponseEntity<Object>(msg, HttpStatus.OK);
        }
    }

    @RequestMapping(value="/s/{id}", method=RequestMethod.GET)
    public void getFullUrl(HttpServletResponse response, @PathVariable("id") String id) throws IOException {
        URLShortenerGrpcClient grpcClient = new URLShortenerGrpcClient();
        response.sendRedirect(grpcClient.getFullURL(id));
    }
}
