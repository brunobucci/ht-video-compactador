package com.br.fiap.postech.ht_video_compactador.application.usecase;

import java.io.File;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @Value("${caminhoDaPastaDeFrames}") 
    String caminhoDaPastaDeFrames;
    
    public CompactarPasta(ICompactacaoQueueAdapterOUT compactacaoQueueAdapterOUT) {
        this.compactacaoQueueAdapterOUT = compactacaoQueueAdapterOUT;
    }
    
	@Override
	public void executar(VideoDto videoDto) {
		
		boolean compactaOk = false;
		try {
			logger.info("Iniciou processo de compactação de pasta.");

			compactaOk = compactaDiretorioDosFrames(caminhoDaPastaDeFrames + videoDto.getCodigoEdicao());
			if(compactaOk) {
				compactacaoQueueAdapterOUT.publishVideoProcessado(toVideoMessage(videoDto, true));
				logger.info("Finalizou processo de compactação de pasta.");
			}
			else {
				compactacaoQueueAdapterOUT.publishVideoProcessado(toVideoMessage(videoDto, false));
				compactacaoQueueAdapterOUT.publishVideoComErro(toVideoMessage(videoDto, false));
				logger.error("Video publicado nas filas videos_processados e videos_com_notificacao com erro,");
			}
		}
		catch(Exception ex) {
			logger.error("Video publicado nas filas videos_processados e videos_com_notificacao com erro: ", ex);
		}		
	}

	@SuppressWarnings({ "resource" }) 
	public boolean compactaDiretorioDosFrames(String diretorio) {
		try {
			new ZipFile(diretorio+".zip").addFolder(new File(diretorio));
			return true;
		} catch (ZipException e) {
			logger.error("Video publicado na fila videos_com_erro: ", e);
			e.printStackTrace();
			return false;
		}	
	}

	public String toVideoMessage(VideoDto video, boolean sucesso){
        HashMap<Object, Object> message = new HashMap<>();
        message.put("id",video.getId());
        message.put("nomeVideo",video.getNome());
        message.put("codigoEdicao",video.getCodigoEdicao());
        message.put("tentativasDeEdicao",video.getTentativasDeEdicao());
        if(sucesso) {
        	message.put("statusEdicao",StatusEdicao.FINALIZADA);
        }
        else {
        	message.put("statusEdicao",StatusEdicao.COM_ERRO);
        }
        return new Gson().toJson(message);
    }
	
}
