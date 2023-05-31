package com.example.Multithreading.Controller;

import com.example.Multithreading.Dto.ListRequestDto;
import com.example.Multithreading.Dto.RequestDto;
import com.example.Multithreading.Dto.ResponseDto;
import com.example.Multithreading.Service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RestController
public class Controller {
    @Autowired
    Service service;

    @PostMapping("/getList")
    public CompletableFuture<ResponseEntity<?>> getSquareValue(@RequestBody ListRequestDto listRequestDto) {
        List<CompletableFuture<ResponseDto>> futureList = new ArrayList<>();
        for (RequestDto requestDto : listRequestDto.getRequestDtoList()) {
            CompletableFuture<ResponseDto> future = service.getSquareValueAsync(requestDto);
            futureList.add(future);
        }

        CompletableFuture<Void> allFutures = CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0]));

        return allFutures.thenApply(
                v -> {
                    List<ResponseDto> responseDtoList = futureList.stream()
                            .map(CompletableFuture::join)
                            .collect(Collectors.toList());
                    return ResponseEntity.ok(responseDtoList);
                }
        );
    }


}
