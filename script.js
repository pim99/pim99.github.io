//mascaras para text box cpf, celular e cep

function mascaraCpf(objeto){
    
    if(objeto.value.length == 3)
       objeto.value = objeto.value + '.';
    
    if(objeto.value.length == 7)
    objeto.value = objeto.value + '.';
    
    if(objeto.value.length == 11)
    objeto.value = objeto.value + '-';
}

function mascaraCelular(objeto){

    if(objeto.value.length == 0)
      objeto.value = '(' + objeto.value;
 
    if(objeto.value.length == 3)
       objeto.value = objeto.value + ')';
    
    if(objeto.value.length == 4)
    objeto.value = objeto.value + ' ';
    
    if(objeto.value.length == 6)
    objeto.value = objeto.value + ' '; 

    if(objeto.value.length == 11)
      objeto.value = objeto.value + '-';
 }
 
 function mascaraCep(objeto){
    
    if(objeto.value.length == 5)
       objeto.value = objeto.value + '-';
}

//impedindo de colar no textbox confirmar e-mail
document.getElementById('confirmarEmail').onpaste = function(){
    return false;
}

//Busca o elemento <input> com o valor do CEP
let input = document.getElementById('cep')

//Adiciona um Listener do evento "On Change" no campo CEP
input.addEventListener('change', (e) => {
    //Recupera o valor escrito no campo
    let cep = e.target.value
    
    //Instancia a requisição para o servidor da API
    const request = new Request(`https://viacep.com.br/ws/${cep}/json`)

    fetch(request)
        .then(response => {
            response.json().then((value) => {
            preencheCampos(value)
        })
        .catch(error => {
            console.error(error);
        })
    })

})

let preencheCampos = (data) => {

    const logradouro = document.getElementById("logradouro")
    const uf = document.getElementById("uf")
    const localidade = document.getElementById("localidade")
    const bairro = document.getElementById("bairro")

    logradouro.value = data.logradouro
    uf.value = data.uf
    localidade.value = data.localidade
    bairro.value = data.bairro
}

let formulario = {
    handleSubmit:(event)=>{
        event.prevetDefault();
        let send = true;

        let inputs = form.querySelectorAll('input');

        for(let i=0; i<input.length; i++){
            let input = inputs[i];
            console.log(input);
        }

        send = false;
        if(send){
            form.submit()
        }
    }
};

let form = document.querySelector('.formulario');
form.addEventListener('submit', formulario.handleSubmit);
    
