const usuarioLogado = buscarJsonLocalmente(KEY_USUARIO);

async function inicializar() {
    if (usuarioLogado) {
        getById('saudacao').innerHTML = `Bem vindo ${usuarioLogado.nome} (Conta ${usuarioLogado.tipo})`;

        await criarInteresses('divInteresses');
    } else {
        redirecionarSemHistorico('../login/login.html');
    }
}

async function relatorioTodosUsuarios() {
    const response = await fetch('http://127.0.0.1:8080/api/relatorios/usuarios', {
        method: 'GET',
        headers: { 'Authorization': buscarLocalmente(KEY_TOKEN) }
    });
    if (response.ok) {
        await downloadFile(response, "usuarios.csv");
    } else {
        await mostrarErro(response);
    }
}

async function relatorioUsuariosPorInteresses() {
    const interesses = [];
    document.getElementsByName('interesse').forEach(checkboxInteresse => {
        if (checkboxInteresse.checked) {
            interesses.push(checkboxInteresse.value);
        }
    });
    const queryString = `interesses=${interesses}`;
    const response = await fetch(`http://127.0.0.1:8080/api/relatorios/usuarios/interesses?${queryString}`, {
        method: 'GET',
        headers: { 'Authorization': buscarLocalmente(KEY_TOKEN) }
    });
    if (response.ok) {
        await downloadFile(response, "usuariosInteresses.csv");
    } else {
        await mostrarErro(response);
    }
}


async function downloadFile(response, nomeArquivo) {
    // https://stackoverflow.com/a/42274086/3072570
    // https://stackoverflow.com/a/19328891/3072570
    const csv = await response.blob();
    const url = window.URL.createObjectURL(csv);
    const link = document.createElement("a");
    document.body.appendChild(link);
    link.href = url;
    link.download = nomeArquivo;
    link.click();
    link.remove();
    window.URL.revokeObjectURL(url);
}
