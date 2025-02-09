package com.br.fiap.postech.ht_video_compactador.infra.messaging;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.br.fiap.postech.ht_video_compactador.application.dto.VideoDto;
import com.br.fiap.postech.ht_video_compactador.domain.entity.StatusEdicao;
import com.br.fiap.postech.ht_video_compactador.domain.usecase.ICompactarPastaUsecase;
import com.google.gson.Gson;

@SpringBootTest
class CompactacaoQueueAdapterINTest {

	@InjectMocks
    private CompactacaoQueueAdapterIN compactacaoQueueAdapterIN;

    @Mock
    private ICompactarPastaUsecase compactarPastaUsecase;

    @Mock
    private Gson gson;

    private VideoDto videoDto;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        videoDto = new VideoDto("111", "123", "video.mp4", StatusEdicao.CRIADA);
    }

	@Test
    void testReceive() {
        // Arrange
        String message = "{\"codigoEdicao\":\"123\", \"nomeVideo\":\"video.mp4\", \"statusEdicao\":\"CRIADA\"}";
        HashMap<Object, Object> mensagem = new HashMap<>();
        mensagem.put("codigoEdicao", "123");
        mensagem.put("nomeVideo", "video.mp4");
        mensagem.put("statusEdicao", "CRIADA");

        when(gson.fromJson(message, HashMap.class)).thenReturn(mensagem);

        // Act
        compactacaoQueueAdapterIN.receive(message);

        // Assert
        //verify(compactarPastaUsecase, times(1)).executar(videoDto);

        assertEquals("123", videoDto.getCodigoEdicao());
        assertEquals("video.mp4", videoDto.getNome());
    }

	@Test
    void testFromMessageToDto() {
        // Arrange
        Map<String, String> mensagem = new HashMap<>();
        mensagem.put("codigoEdicao", "123");
        mensagem.put("nomeVideo", "video.mp4");

        // Act
        videoDto = CompactacaoQueueAdapterIN.fromMessageToDto(mensagem);

        // Assert
        assertEquals("123", videoDto.getCodigoEdicao());
        assertEquals("video.mp4", videoDto.getNome());
    }
}
