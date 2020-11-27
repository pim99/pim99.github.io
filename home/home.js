let usuarioLocal = buscarJsonLocalmente(KEY_USUARIO);

async function inicializar() {
    if (usuarioLocal) {
        const response = await fetch(`http://127.0.0.1:8080/api/usuarios/${usuarioLocal.id}`, {
            method: 'GET',
            headers: { 
                'Content-Type': 'application/json',
                'Authorization': buscarLocalmente(KEY_TOKEN)
            }
        });

        if (response.ok) {
            const usuarioRemoto = await response.json();
            await preencherDados(usuarioRemoto);
        } else {
            await mostrarErro(response);
        }
    } else {
        redirecionarSemHistorico('../login/login.html');
    }
}

async function preencherDados(usuarioRemoto) {
    try {
        usuarioLocal = usuarioRemoto;
        salvarJsonLocalmente(KEY_USUARIO, usuarioLocal);

        getById('saudacao').innerHTML = `Bem vindo ${usuarioLocal.nome} (Conta ${usuarioLocal.tipo})`;
        // Preenche os dados do usuário logado nos campos da tela (HTML)
        getById('nome').value = usuarioLocal.nome;
        getById('email').value = usuarioLocal.email;
        getById('documento').value = usuarioLocal.documento;
        getById('telefone').value = usuarioLocal.telefone;
        getById('radioPf').checked = usuarioLocal.tipo == 'PF';
        getById('radioPj').checked = usuarioLocal.tipo == 'PJ';
        // Buscar todos os interesses possíveis
        await criarInteresses('interesses', usuarioLocal);
    } catch (error) {
        console.log(error);
        alert('Ocorreu um erro inesperado!');
    }
}

async function salvar() {
    try {
        // Atribui ao JSON os valores dos campos recuperados do HTML (DOM):
        usuarioLocal.nome = getById('nome').value;
        usuarioLocal.documento = getById('documento').value;
        // Realiza um tratamento especifico para os inputs do tipo 'radio':
        const radioPf = getById('radioPf');
        const radioPj = getById('radioPj');
        usuarioLocal.tipo = radioPf.checked ? radioPf.value : radioPj.value;
        usuarioLocal.telefone = getById('telefone').value;
        // Identifica os interesses selecionados e os relaciona com o usuário logado
        usuarioLocal.interesses = []
        document.getElementsByName('interesse').forEach(checkboxInteresse => {
            if (checkboxInteresse.checked) {
                const interesse = {
                    id: checkboxInteresse.value
                }
                usuarioLocal.interesses.push(interesse);
            }
        });
        // Consome a API (rodando localmente) para a alteração do usuario logado.
        const response = await fetch(`http://127.0.0.1:8080/api/usuarios/${usuarioLocal.id}`, {
            method: 'PUT',
            headers: { 
                'Content-Type': 'application/json',
                'Authorization': buscarLocalmente(KEY_TOKEN)
            },
            body: JSON.stringify(usuarioLocal)
        });

        // Verifica a resposta da API - Sucesso (2XX) ou Falha (4XX)):
        if (response.ok) {
            const usuarioAlterado = await response.json();
            preencherDados(usuarioAlterado);
            alert('Dados alterados com sucesso!');
        } else {
            await mostrarErro(response);
        }
    } catch (error) {
        console.log(error);
        alert('Ocorreu um erro inesperado!');
    }

}

function logoff() {
    removerLocalmente(KEY_USUARIO, KEY_TOKEN);
    redirecionarSemHistorico('../login/login.html');
}