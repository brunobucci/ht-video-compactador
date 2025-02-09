package com.br.fiap.postech.ht_video_compactador.infra.messaging;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.br.fiap.postech.ht_video_compactador.application.dto.VideoDto;
import com.br.fiap.postech.ht_video_compactador.domain.repository.ICompactacaoQueueAdapterIN;
import com.br.fiap.postech.ht_video_compactador.domain.usecase.ICompactarPastaUsecase;
import com.google.gson.Gson;

@Service
public class CompactacaoQueueAdapterIN implements ICompactacaoQueueAdapterIN{

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private Gson gson;
	
	private ICompactarPastaUsecase compactarPastaUsecase;
	
	public CompactacaoQueueAdapterIN(ICompactarPastaUsecase compactarPastaUsecase) {
		this.compactarPastaUsecase = compactarPastaUsecase;
	}
	
	@SuppressWarnings("unchecked")
	@RabbitListener(queues = {"${queue1.name}"})
	@Override
	public void receive(@Payload String message) {
		HashMap<String, String> mensagem = gson.fromJson(message, HashMap.class);
		VideoDto videoDto = fromMessageToDto(mensagem);
		
		compactarPastaUsecase.executar(videoDto);
		
	}
	
	static VideoDto fromMessageToDto(Map<String, String> mensagem) {
		return new VideoDto(
				mensagem.get("id"),
				mensagem.get("codigoEdicao"),
				mensagem.get("nomeVideo"), 
				null);
	}
}
