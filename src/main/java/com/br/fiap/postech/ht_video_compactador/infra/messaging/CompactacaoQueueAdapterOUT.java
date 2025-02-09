package com.br.fiap.postech.ht_video_compactador.infra.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.br.fiap.postech.ht_video_compactador.domain.repository.ICompactacaoQueueAdapterOUT;

@Service
public class CompactacaoQueueAdapterOUT implements ICompactacaoQueueAdapterOUT{

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Value("${queue2.name}") 
	String filaVideosProcessados;
	
	@Value("${queue3.name}") 
	String filaVideosComErro;
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@Override
	public void publishVideoProcessado(String videoJson) {
		rabbitTemplate.convertAndSend(filaVideosProcessados, videoJson);
		logger.info("Publicação na fila videos_processados executada.");
	}

	@Override
	public void publishVideoComErro(String videoJson) {
		rabbitTemplate.convertAndSend(filaVideosComErro, videoJson);
		logger.info("Publicação na fila videos_com_notificacao executada.");
	}
}
