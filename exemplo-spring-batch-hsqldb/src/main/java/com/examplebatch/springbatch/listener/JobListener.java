package com.examplebatch.springbatch.listener;

import com.examplebatch.springbatch.entitys.PessoaEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class JobListener extends JobExecutionListenerSupport {

    private static final Logger LOG = LoggerFactory.getLogger(JobListener.class);

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public JobListener(JdbcTemplate jdbcTemplate){
        super();
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED){
            LOG.info("TERMINOU O TRABALHO! OLHE OS RESULTADOS:");

            jdbcTemplate.query(
                    "SELECT nome, sobrenome, telefone FROM pessoa_table",(rs, row) -> new PessoaEntity(rs.getString(1),
                    rs.getString(2), rs.getString(3)))
                    .forEach(pessoaEntity -> LOG.info("REGISTRO < " + pessoaEntity + " >"));
        }
    }
}
