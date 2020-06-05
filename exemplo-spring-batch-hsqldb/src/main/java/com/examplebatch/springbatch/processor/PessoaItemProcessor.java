package com.examplebatch.springbatch.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.examplebatch.springbatch.entitys.PessoaEntity;

public class PessoaItemProcessor implements ItemProcessor<PessoaEntity, PessoaEntity> {

    private static final Logger LOG = LoggerFactory.getLogger(PessoaItemProcessor.class);

    @Override
    public PessoaEntity process(PessoaEntity item) throws Exception {
        String nome = item.getNome().toUpperCase();
        String sobrenome = item.getSobrenome().toUpperCase();
        String telefone = item.getTelefone();

        PessoaEntity pessoaEntity = new PessoaEntity(nome, sobrenome, telefone);

        LOG.info("Convertendo (" + item + ") a (" + pessoaEntity + ")");

        return pessoaEntity;
    }
}
