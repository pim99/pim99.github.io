const usuarioLogado = buscarJsonLocalmente(KEY_USUARIO);

async function buscarUsuarios() {
    if (usuarioLogado) {
        try {
            getById('saudacao').innerHTML = `Bem vindo ${usuarioLogado.nome} (Conta ${usuarioLogado.tipo})`;
    
            const response = await fetch('http://127.0.0.1:8080/api/usuarios', {
                method: 'GET',
                headers: { 'Authorization': buscarLocalmente(KEY_TOKEN) }
            });
    
            if (response.ok) {
                // Recupera o container (div) destinado aos usuarios e o deixa vazio (será populado dinamicamente)
                const gridUsuarios = getById('gridUsuarios');
                gridUsuarios.innerHTML = '';

                const usuarios = await response.json();

                for (let i = 0; i < usuarios.length; i++) {
                    const usuario = usuarios[i];
                    const id = criarElementoComClasse('div', usuario.id, 'col-1', 'center');
                    const nome = criarElementoComClasse('div', usuario.nome, 'col-4', 'center');
                    const email = criarElementoComClasse('div', usuario.email, 'col-4', 'center');
                    const tipo = criarElementoComClasse('div', usuario.tipo, 'col-1', 'center');

                    const acoes = criarElementoComClasse('div', '', 'col-2', 'center');
                    const iconeVisualizar = criarElemento('img');
                    iconeVisualizar.classList.add('icon');
                    iconeVisualizar.src = '../base/icons/view.svg';
                    iconeVisualizar.onclick = function(){
                        visualizarusuario(usuario);
                    };
                    acoes.appendChild(iconeVisualizar);

                    const estiloLinha = i%2 == 0 ? 'row-pair' : 'row-odd';
                    const estiloBloqueado = usuario.bloqueado ? 'row-blocked' : '';
                    const linha = criarElementoComClasse('div', '', 'row', estiloLinha, estiloBloqueado);
                    [id, nome, email, tipo, acoes].forEach(coluna => {
                        linha.appendChild(coluna);
                    });
                    gridUsuarios.appendChild(linha);
                }
            } else {
                await mostrarErro(response);
            }
        } catch (error) {
            console.log(error);
            alert('Ocorreu um erro inesperado!');
            buscarUsuarios();
        }
    } else {
        redirecionarSemHistorico('../login/login.html');
    }
}

function visualizarusuario(usuario) {
    const dialog = getById('usuarioDialog');
    getById('dialogNome').innerHTML = usuario.nome;
    getById('dialogDocumento').innerHTML = usuario.documento;
    getById('dialogTipo').innerHTML = usuario.tipo;
    getById('dialogEmail').innerHTML = usuario.email;
    getById('dialogTelefone').innerHTML = usuario.telefone ? usuario.telefone : 'N/A';
    const interesses = usuario.interesses.map(e => e.descricao).join(', ');
    getById('dialogInteresses').innerHTML = interesses ? interesses : 'N/A';

    const btnDesBloquear = getById('dialogBtnDesBloquear');
    const acao = usuario.bloqueado ? 'desbloquear' : 'bloquear';
    btnDesBloquear.innerHTML = acao;
    if (usuario.bloqueado) {
        btnDesBloquear.classList.remove('warning');
    } else {
        btnDesBloquear.classList.add('warning');
    }
    btnDesBloquear.onclick = function () {
        const confirmou = confirm(`Deseja ${acao} o usuário ${usuario.nome}?`);
        if (confirmou) {
            bloquearUsuario(usuario);
        }
        dialog.close();
    };

    getById('dialogBtnFechar').onclick = function () {
        dialog.close();
    };

    dialog.showModal();
}

async function bloquearUsuario(usuario) {
    try {
        const response = await fetch(`http://127.0.0.1:8080/api/usuarios/${usuario.id}`, {
            method: 'PATCH',
            headers: { 'Authorization': buscarLocalmente(KEY_TOKEN) }
        });

        if (response.ok) {
            alert(`Usuario ${usuario.bloqueado ? 'des' : ''}bloqueado com sucesso!`);
            buscarUsuarios();
        } else {
            await mostrarErro(response);
        }

        } catch (error) {
            console.log(error);
            alert('Ocorreu um erro inesperado!');
        }
}

function criarElementoComClasse(tag, text, ...cssClasses) {
    const elemento = criarElemento(tag);
    if (text) {
        elemento.innerHTML = text;
    }
    cssClasses.forEach(cssClass => {
        if (cssClass) {
            elemento.classList.add(cssClass);
        }
    })
    return elemento;
}

function logoff() {
    removerLocalmente(KEY_USUARIO, KEY_TOKEN);
    redirecionarSemHistorico('../login/login.html');
}