package com.br.fiap.postech.ht_video_compactador.domain.repository;

import org.springframework.messaging.handler.annotation.Payload;

public interface ICompactacaoQueueAdapterIN {
	void receive(@Payload String message);
}
