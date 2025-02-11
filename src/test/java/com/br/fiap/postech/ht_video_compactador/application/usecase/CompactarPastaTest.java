package com.br.fiap.postech.ht_video_compactador.application.usecase;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import com.br.fiap.postech.ht_video_compactador.application.dto.VideoDto;
import com.br.fiap.postech.ht_video_compactador.domain.repository.ICompactacaoQueueAdapterOUT;
import com.google.gson.Gson;

class CompactarPastaTest {

    @InjectMocks
    private CompactarPasta compactarPasta;

    @Mock
    private ICompactacaoQueueAdapterOUT compactacaoQueueAdapterOUT;

    @Mock
    private Gson gson;

    @Mock
    private Logger logger;

    @Mock
    private VideoDto videoDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testExecutar_FalhaNaCompactacao() {
    	// Preparando o comportamento do método privado usando doReturn
    	compactarPasta = spy(new CompactarPasta(compactacaoQueueAdapterOUT));
    	String caminho = "./arquivos/pasta_de_teste";
        doReturn(false).when(compactarPasta).compactaDiretorioDosFrames(caminho);

        // Dados de entrada
        VideoDto videoDto = new VideoDto();
        videoDto.setCodigoEdicao("12345");

        // Chamando o método de execução
        compactarPasta.executar(videoDto);

        // Verificar se as mensagens de erro foram publicadas corretamente
        verify(compactacaoQueueAdapterOUT, times(1)).publishVideoProcessado(anyString());
        verify(compactacaoQueueAdapterOUT, times(1)).publishVideoComErro(anyString());
    }

    @Test
    void testExecutar_ExceptionDuranteCompactacao() {
        // Dados de entrada para o teste
        String codigoEdicao = "123";
        String nomeVideo = "video.mp4";
        when(videoDto.getCodigoEdicao()).thenReturn(codigoEdicao);
        when(videoDto.getNome()).thenReturn(nomeVideo);
        when(videoDto.getId()).thenReturn("1");
        when(videoDto.getTentativasDeEdicao()).thenReturn("0");

        // Simular exceção durante compactação
        String caminho = "./arquivos/pasta_de_teste";
        // Usando o spy para mockar método privado
        CompactarPasta spyCompactarPasta = spy(compactarPasta);
        doThrow(new RuntimeException("Erro de compactação")).when(spyCompactarPasta).compactaDiretorioDosFrames(caminho);

        // Chamada ao método
        spyCompactarPasta.executar(videoDto);

        // Verificação de chamadas para a fila em caso de erro
        verify(compactacaoQueueAdapterOUT).publishVideoProcessado(anyString());
        verify(compactacaoQueueAdapterOUT).publishVideoComErro(anyString());
    }
}
