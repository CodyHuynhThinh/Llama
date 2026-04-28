package com.llm.date1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
public class LlmService {
    @Autowired
    private WebClient webClient;

    @Autowired
    private PdfService pdfService;

    @Autowired
    private TextChunker chunker;

    public String ask(String question) {

//        Map<String, Object> body = Map.of(
//                "model", "gpt-4o-mini",
//                "messages", List.of(
//                        Map.of("role", "user", "content", question)
//                )
//        );
//
//        return webClient.post()
//                .uri("/v1/chat/completions")
//                .bodyValue(body)
//                .retrieve()
//                .bodyToMono(String.class)
//                .onErrorResume(e -> {
//                    e.printStackTrace();
//                    return Mono.just("ERROR: " + e.getMessage());
//                })
//                .doOnNext(System.out::println)
//                .block();

        Map<String, Object> body = Map.of(
                "model", "llama3",
                "prompt", question,
                "stream", false
        );

        return webClient.post()
                .uri("/api/generate")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Map.class)
                .map(res -> res.get("response").toString())
                .block();
    }

    public String askWithPdf(String question) {
        String text = pdfService.readPdf("D:/expert_oracle_database_architecture.pdf");
        List<String> chunks = chunker.chunk(text, 1000);

        // tìm đoạn liên quan
        String context = chunks.stream()
                .filter(c -> c.toLowerCase().contains(question.toLowerCase()))
                .findFirst()
                .orElse(chunks.get(0));

        String prompt = "Answer based on this context:\n"
                + context + "\n\nQuestion: " + question;

        Map<String, Object> body = Map.of(
                "model", "phi3", // 🔥 nhớ đổi model local
                "prompt", prompt,
                "stream", false
        );

        return webClient.post()
                .uri("/api/generate")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Map.class)
                .map(res -> res.get("response").toString())
                .block();
    }
}
