# Lemobs Challenge
Repositório contendo o código fonte do desafio técnico, o qual fui submetido, para vaga de estágio na Lemobs.

## Visualização do Aplicativo Pronto 
<img src="screen.gif" width="240"/>

## O Desafio
Consiste em criar um aplicativo android de uma biblioteca de livros virtuais.
Deve ser utilizada a [IDE Android Studio](https://developer.android.com/studio), utilizando a
linguagem Kotlin.

O aplicativo tem duas telas. A lista de livros do usuário e a lista de livros à venda. O
aplicativo começa na lista de livros do usuário. Ao iniciar o aplicativo pela primeira vez, essa
lista está vazia.
O usuário começa com R$ 100,00. A informação de dinheiro do usuário deve ser
exibida em todas as telas. O usuário pode ir para a lista de livros à venda ao apertar o botão
"Comprar Livros".
Tela - Meus Livros
O aplicativo deve mostrar a lista de livros do usuário. Um livro tem as seguintes
informações:
* Nome [title]
* Escritor [writer]
* Foto do item [thumbnailHd]

Tela - Livros à Venda
O aplicativo deve mostrar a lista de livros à venda. Para obter os livros da loja, sua
aplicação deverá realizar uma chamada GET na [URL](
https://raw.githubusercontent.com/Felcks/desafio-mobile-lemobs/master/products.json). Um
livro à venda tem as seguintes informações:
* Nome [title]
* Preço [price]
* Escritor [writer]
* Foto do item [thumbnailHd]

Cada item possui um botão de comprar. O aplicativo só aprova a compra se o
usuário tiver dinheiro suficiente. Se confirmada a compra, o valor deve ser descontado do
dinheiro do usuário. Livros já comprados não aparecem na lista à venda ou aparecem como
bloqueados.

Além disso, foram dadas algumas funcionalidades extras que poderiam ser implementadas:
* Conseguir dar Zoom na imagem dos livros.
* Criatividade na criação das telas do aplicativo.
* Uso de padrões arquiteturais.
* Ter uma opção de favoritar um livro na lista de livros à venda. Livros favoritados
aparecem no topo da lista.
* Ter uma opção de atualizar a lista de livros à venda na toolbar do aplicativo.
* Ter a opção de busca pelo nome na lista de livros à venda.
* Usar Kotlin.
* Uso de Mvp ou de MVVM.
* Uso de SharedPreferences para salvar o dinheiro.

## Andamento
### Requisitos Obrigatórios
- [x] Tela para listar livros comprados.
- [x] Tela para listar livros disponíveis na loja.
- [x] Mostrar saldo em ambas as telas.
- [x] Requisitar os livros através de uma chamada GET e os exibir na página de compra.
- [x] Possibilidade de compra dos livros.
- [x] Exibição dos livros comprados na tela Meus Livros.
### Funcionalidades Extras
- [x] Conseguir dar Zoom na imagem dos livros.
- [x] Criatividade na criação das telas do aplicativo.
- [x] Uso de padrões arquiteturais.
- [x] Ter uma opção de favoritar um livro na lista de livros à venda. Livros favoritados
aparecem no topo da lista.
- [x] Ter uma opção de atualizar a lista de livros à venda na toolbar do aplicativo.
- [x] Ter a opção de busca pelo nome na lista de livros à venda.
- [x] Usar Kotlin.
- [x] Uso de Mvp ou de MVVM.
- [x] Uso de SharedPreferences para salvar o dinheiro.
### Possiveis Melhorias
- [x] Mensagem mostrando o êxito ou a falha na compra.
- [x] O uso de `Fragments` para a transição entre as telas.
