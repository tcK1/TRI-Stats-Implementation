# Teoria de Resposta ao Item - [Enunciado](https://github.com/tcK1/EP-IE-2016/blob/master/info/EP.pdf)
Exercício de programação de Introdução a Estatística - 1º Semestre / 2016

A Teoria de Resposta ao Item (TRI) considera o seguinte modelo teórico para descrever a probabilidade de um aluno j acertar a questão i:

Pr(Aij = 1) = e^ai(θj−bi) / 1 + e^ai(θj−bi),

onde Aij ∈ {0, 1} é a variável aleatória que indica se o aluno j acertou (Aij = 1) ou errou (Aij = 0) a questão i, θj representa a habilidade do aluno j, ai representa o parâmetro de discriminação da questão i, e bi representa o parâmetro de dificuldade da questão i.

## Proposta

Dados os arquivos iniciais: "questoes.txt" e "respostas.txt", calcular:

1. Selecionando o melhor aluno
2. Selecionando a melhor prova
3. Intervalo de confiança
4. Estimador Pontual - Alunos
5. Selecionando o melhor aluno - Habilidade do Aluno
6. Intervalo de confiança - Habilidade do Aluno
7. Intervalo de confiança - Aproximando a uma Normal

**Descrição dos diretórios do projeto:**
```
/bin/: Executáveis gerados
/src/: Códigos fonte
/res/: Arquivos de entrada do programa
/out/: Arquivos de saida gerados
/info/: Arquivos de informações
```
