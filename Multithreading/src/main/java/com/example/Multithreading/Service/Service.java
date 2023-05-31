package com.example.Multithreading.Service;

import com.example.Multithreading.Dto.RequestDto;
import com.example.Multithreading.Dto.ResponseDto;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.CompletableFuture;

@org.springframework.stereotype.Service
public class Service {

    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;

    public Service(ThreadPoolTaskExecutor threadPoolTaskExecutor) {
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
    }

    @Async
    public CompletableFuture<ResponseDto> getSquareValueAsync(RequestDto requestDto) {
        CompletableFuture<ResponseDto> future = new CompletableFuture<>();
        ListenableFuture<ResponseDto> listenableFuture = new AsyncResult<>(null);

        threadPoolTaskExecutor.execute(() -> {
            ResponseDto responseDto = new ResponseDto();
            responseDto.setGivenNumber(requestDto.getNumber());
            responseDto.setSquareNumber(requestDto.getNumber() * requestDto.getNumber());
            future.complete(responseDto);
        });

        listenableFuture.addCallback(
                future::complete,
                ex -> future.completeExceptionally(ex)
        );
        System.out.println(Thread.currentThread().getName());
        return future;
    }

}
