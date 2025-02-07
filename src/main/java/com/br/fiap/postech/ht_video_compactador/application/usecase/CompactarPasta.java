package com.br.fiap.postech.ht_video_compactador.application.usecase;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.br.fiap.postech.ht_video_compactador.application.dto.VideoDto;
import com.br.fiap.postech.ht_video_compactador.domain.entity.StatusEdicao;
import com.br.fiap.postech.ht_video_compactador.domain.repository.ICompactacaoQueueAdapterOUT;
import com.br.fiap.postech.ht_video_compactador.domain.usecase.ICompactarPastaUsecase;
import com.google.gson.Gson;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;

@Service
public class CompactarPasta implements ICompactarPastaUsecase{

private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
    private final ICompactacaoQueueAdapterOUT compactacaoQueueAdapterOUT;

    @Autowired
    private Gson gson;
    
    @Value("${caminhoDaPastaDeFrames}")
    private String caminhoDaPastaDeFrames;
    
    public CompactarPasta(ICompactacaoQueueAdapterOUT compactacaoQueueAdapterOUT) {
        this.compactacaoQueueAdapterOUT = compactacaoQueueAdapterOUT;
    }
    
	@Override
	public void executar(VideoDto videoDto) {
		try {
			logger.info("Iniciou processo de compactação de pasta.");

			compactaDiretorioDosFrames(caminhoDaPastaDeFrames + videoDto.getCodigoEdicao());
			
			videoDto.setStatusEdicao(StatusEdicao.COMPACTADA);
			compactacaoQueueAdapterOUT.publishVideoCompactado(toVideoMessage(videoDto));
			
			logger.info("Finalizou processo de compactação de pasta.");
		}
		catch(Exception ex) {
			//TODO - Publicar na fila de video com erro
			//extracaoQueueAdapterOUT.publishVideoProcessado(toVideoMessage(videoDto));
			logger.error("Video publicado na fila videos_com_erro: ", ex);
		}		
	}

	@SuppressWarnings({ "resource" })
	private void compactaDiretorioDosFrames(String diretorio) {
		try {
			new ZipFile(diretorio+".zip").addFolder(new File(diretorio));
		} catch (ZipException e) {
			logger.error("Video publicado na fila videos_com_erro: ", e);
			e.printStackTrace();
		}	
	}

	private String toVideoMessage(VideoDto video){
        Map message = new HashMap<String, String>();
        message.put("nomeVideo",video.getNome());
        message.put("codigoEdicao",video.getCodigoEdicao().toString());
        message.put("statusEdicao",StatusEdicao.FINALIZADA);
        return gson.toJson(message);
    }
	
}
