const KEY_USUARIO = 'usuario';
const KEY_TOKEN = 'token';

function getById(id) {
  return document.getElementById(id);
}

function criarElemento(tag) {
  return document.createElement(tag);
}

function redirecionar(pagina) {
  // https://stackoverflow.com/a/1655081/3072570
  location.href = pagina;
}

function redirecionarSemHistorico(pagina) {
  // https://stackoverflow.com/a/9980166/3072570
  location.replace(pagina);
}

// Funções uteis para salvar e recuperar dados primitivos no localStorage.
// Referência: https://www.w3schools.com/jsref/prop_win_localstorage.asp

function salvarLocalmente(chave, valor) {
  localStorage.setItem(chave, valor);
}

function buscarLocalmente(chave) {
  return localStorage.getItem(chave);
}

function removerLocalmente(...chaves) {
  chaves.forEach(chave => {
    localStorage.removeItem(chave);
  });
}

// Funções uteis para salvar e recuperar objetos JSON no localStorage.
// Referência: https://stackoverflow.com/a/2010948/3072570

function salvarJsonLocalmente(chave, valor) {
  localStorage.setItem(chave, JSON.stringify(valor));
}

function buscarJsonLocalmente(chave) {
  return JSON.parse(localStorage.getItem(chave));
}

// Função útil para a criação de um Token de Basic Authentication.
// Referência: https://stackoverflow.com/a/51500400/3072570

function gerarTokenBasicAuth(email, senha) {
  let hashBase64 = btoa(`${email}:${senha}`);
  return `Basic ${hashBase64}`;
}

// Função comum às telas de Home (PF e PJ) e Relatórios.
async function criarInteresses(idContainer, usuarioLocal) {
  const response = await fetch('http://127.0.0.1:8080/api/interesses', {
    method: 'GET',
    headers: { 'Authorization': buscarLocalmente(KEY_TOKEN) }
  });
  if (response.ok) {
    // Recupera o container (div) destinado aos interesses, deixando-o vazio.
    const divInteresses = getById(idContainer);
    divInteresses.innerHTML = '';

    // Recupera do response a lista de interesses (array de JSON)
    const interesses = await response.json();
    interesses.forEach(interesse => {
      // Cria um checkbox dinamicamente
      const checkbox = criarElemento('input');
      checkbox.id = interesse.descricao;
      checkbox.name = 'interesse';
      checkbox.type = 'checkbox';
      checkbox.value = interesse.id;
      if (usuarioLocal) {
        checkbox.checked = usuarioLocal.interesses.some(item => item['id'] === interesse.id);
      }
      // Cria uma label dinamicamente
      const label = criarElemento('label');
      label.htmlFor = checkbox.id;
      label.innerHTML = interesse.descricao;
      // Inclui os elementos no container (div) destinado aos interesses.
      divInteresses.appendChild(checkbox);
      divInteresses.appendChild(label);
    });
  } else {
    await mostrarErro(response);
  }
}

async function mostrarErro(response) {
  const erro = await response.json();
  alert(erro.mensagem ? erro.mensagem : 'Ocorreu um erro inesperado, entre em contato com o administrador.');
}