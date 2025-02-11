package com.br.fiap.postech.ht_video_compactador.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.br.fiap.postech.ht_video_compactador.application.dto.VideoDto;
import com.br.fiap.postech.ht_video_compactador.domain.entity.StatusEdicao;
import com.br.fiap.postech.ht_video_compactador.infra.messaging.CompactacaoQueueAdapterOUT;
import com.google.gson.Gson;

@SpringBootTest
class CompactarPastaTest {

	@InjectMocks
	private CompactarPasta compactarPasta;

	@Mock
	private CompactacaoQueueAdapterOUT compactacaoQueueAdapterOUT;

	@Mock
	private Gson gson;

	private VideoDto videoDto;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		videoDto = new VideoDto("111", "123456", "nome-do-video.mp4", StatusEdicao.COMPACTADA);
	}

	@Test
	void testExecutar_Success() {
		// Arrange
		String caminhoDaPastaDeFrames = "/Users/marceloquevedo/Desktop/pos/hack/teste/";
		compactarPasta.caminhoDaPastaDeFrames = caminhoDaPastaDeFrames;
	
		// Act
		compactarPasta.executar(videoDto);
	
		// Assert
		assertEquals(StatusEdicao.COMPACTADA, videoDto.getStatusEdicao());
		//verify(compactarPasta, times(1)).executar(videoDto);
	}
	
	@Test
	void testeExecutar_Exception() {
		// Arrange
		String caminhoDaPastaDeFrames = "/Users/marceloquevedo/Desktop/pos/hack/teste/";
		compactarPasta.caminhoDaPastaDeFrames = caminhoDaPastaDeFrames;
	
		doThrow(new RuntimeException("Erro ao compactar")).when(compactacaoQueueAdapterOUT).publishVideoComErro(anyString());
	
		// Act
		compactarPasta.executar(videoDto);
	
		// Assert
		assertEquals(StatusEdicao.COMPACTADA, videoDto.getStatusEdicao());
		//verify(compactarPasta, times(1)).executar(videoDto);
		//verify(compactacaoQueueAdapterOUT, times(1)).publishVideoCompactado(anyString());
	}
}
