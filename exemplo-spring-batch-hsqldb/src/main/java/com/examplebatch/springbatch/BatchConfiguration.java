package com.examplebatch.springbatch;

import com.examplebatch.springbatch.entitys.PessoaEntity;
import com.examplebatch.springbatch.listener.JobListener;
import com.examplebatch.springbatch.processor.PessoaItemProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Bean
    public FlatFileItemReader<PessoaEntity> reader() {
        FlatFileItemReader<PessoaEntity> reader = new FlatFileItemReaderBuilder<PessoaEntity>()
                .name("pessoaItemReader")
                .resource(new ClassPathResource("sample-data.csv"))
                .delimited()
                .names(new String[]{"nome", "sobrenome", "telefone"})
                .fieldSetMapper(new BeanWrapperFieldSetMapper<PessoaEntity>() {{
                    setTargetType(PessoaEntity.class);
                }})
                .build();
        return reader;
    }

    @Bean
    public PessoaItemProcessor processor() {
        return new PessoaItemProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<PessoaEntity> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<PessoaEntity>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO pessoa_table (nome, sobrenome, telefone) VALUES (:nome, :sobrenome, :telefone)")
                .dataSource(dataSource)
                .build();

    }

    @Bean
    public Job importPessoaJob(JobListener listener, Step step1) {
        return jobBuilderFactory.get("importPessoaJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1)
                .end()
                .build();
    }

    @Bean
    public Step step1(JdbcBatchItemWriter<PessoaEntity> writer) {
        return stepBuilderFactory.get("step1")
                .<PessoaEntity, PessoaEntity>chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer)
                .build();
    }
}
