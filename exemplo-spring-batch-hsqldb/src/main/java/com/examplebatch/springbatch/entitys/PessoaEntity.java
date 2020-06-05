package com.examplebatch.springbatch.entitys;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@ToString
public class PessoaEntity {

    private String nome;
    private String sobrenome;
    private String telefone;


}