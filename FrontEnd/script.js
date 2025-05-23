let currentStep = 1;
const totalSteps = 6;

function updateProgress() {
    const progressFill = document.getElementById('progressFill');
    progressFill.style.width = `${((currentStep - 1) / (totalSteps - 1)) * 100}%`;
    
    document.querySelectorAll('.progress-step').forEach((step, index) => {
        if (index + 1 <= currentStep) {
            step.classList.add('active');
        } else {
            step.classList.remove('active');
        }
    });
}

function showStep(stepNumber) {
    document.querySelectorAll('.form-step').forEach(step => {
        step.classList.remove('active');
    });
    
    document.getElementById(`step${stepNumber}`).classList.add('active');
    currentStep = stepNumber;
    updateProgress();
}

function nextStep(stepNumber) {
    if (stepNumber === 2) {
        if (!document.getElementById('data').value || 
            !document.getElementById('executante').value || 
            !document.getElementById('legal').value) {
            alert('Preencha todos os campos obrigatórios!');
            return;
        }
    } else if (stepNumber === 3) {
        if (!document.getElementById('rota').value || 
            !document.getElementById('descricao').value) {
            alert('Preencha todos os campos obrigatórios!');
            return;
        }
    } else if (stepNumber === 4) {
        if (!document.getElementById('tipo').value || 
            !document.getElementById('ct').value || 
            !document.getElementById('caboNumero').value || 
            !document.getElementById('lateralNumero').value || 
            !document.getElementById('atpOsx').value) {
            alert('Preencha todos os campos obrigatórios!');
            return;
        }
    } else if (stepNumber === 5) {
        const inspecaoItems = [
            'caboAereo', 
            'caboSubterraneo', 
            'pressurizacao',
            'instalacao_padrao', 
            'linhaTerra', 
            'pavimento',
            'medicao',
            'limpeza'
        ];
        
        for (const item of inspecaoItems) {
            if (!document.querySelector(`input[name="${item}"]:checked`)) {
                alert(`Preencha o item de inspeção: ${item}`);
                return;
            }
        }
    }
    showStep(stepNumber);
}

function prevStep(stepNumber) {
    showStep(stepNumber);
}

function addMaterial() {
    const container = document.getElementById('materiaisContainer');
    const newRow = document.createElement('div');
    newRow.className = 'material-row';
    newRow.innerHTML = `
        <input type="text" placeholder="Descrição do material" class="material-desc">
        <input type="number" placeholder="Quantidade" class="material-qtd">
        <button type="button" onclick="this.parentNode.remove()" class="remove-material">×</button>
    `;
    container.appendChild(newRow);
}

async function submitForm() {
    const loading = document.getElementById('loading');
    loading.classList.add('active');
    
    try {
        // Validação final
        if (!document.getElementById('coordenador').value) {
            throw new Error('Preencha o nome do coordenador!');
        }
        
        const nomeArquivoInput = document.getElementById('nomeArquivo').value.trim();
        if (!nomeArquivoInput) {
            throw new Error('Defina um nome para o arquivo PDF!');
        }
        
        // Garante a extensão .pdf e remove caracteres inválidos
        let nomeArquivo = nomeArquivoInput;
        if (!nomeArquivo.toLowerCase().endsWith('.pdf')) {
            nomeArquivo += '.pdf';
        }
        nomeArquivo = nomeArquivo.replace(/[/\\:*?"<>|]/g, '');
        
        // Coletar dados de inspeção
        const inspecaoItems = [
            'caboAereo', 
            'caboSubterraneo', 
            'pressurizacao',
            'instalacao_padrao', 
            'linhaTerra', 
            'pavimento',
            'medicao',
            'limpeza'
        ];
        
        const inspecao = {};
        for (const item of inspecaoItems) {
            const selected = document.querySelector(`input[name="${item}"]:checked`);
            if (!selected) {
                throw new Error(`Preencha o item de inspeção: ${item}`);
            }
            inspecao[item] = selected.value;
        }
        
        // Coletar materiais
        const materiais = Array.from(document.querySelectorAll('.material-row'))
            .map(row => ({
                descricao: row.querySelector('.material-desc').value,
                quantidade: row.querySelector('.material-qtd').value
            }))
            .filter(item => item.descricao && item.quantidade);
        
        // Criar objeto de dados para enviar
        const formData = {
            data: document.getElementById('data').value,
            executante: document.getElementById('executante').value,
            legal: document.getElementById('legal').value,
            rotaEndereco: document.getElementById('rota').value,
            descricaoServicos: document.getElementById('descricao').value,
            tipo: document.getElementById('tipo').value,
            ct: document.getElementById('ct').value,
            caboNumero: document.getElementById('caboNumero').value,
            lateralNumero: document.getElementById('lateralNumero').value,
            atpOsx: document.getElementById('atpOsx').value,
            coordenador: document.getElementById('coordenador').value,
            inspecaoServico: inspecao,
            materiaisGastos: materiais
        };
        
        // Enviar para o backend
        const response = await fetch('https://pdfgenerator-vy8p.onrender.com', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(formData)
        });
        
        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(errorText || 'Erro no servidor');
        }
        
        // Processar o PDF retornado
        const blob = await response.blob();
        const url = window.URL.createObjectURL(blob);
        
        const a = document.createElement('a');
        a.style.display = 'none';
        a.href = url;
        a.download = nomeArquivo;
        
        document.body.appendChild(a);
        a.click();
        
        // Limpeza
        window.URL.revokeObjectURL(url);
        document.body.removeChild(a);
        
        alert('Relatório gerado com sucesso!');
        
    } catch (error) {
        console.error('Erro:', error);
        alert(`Falha ao gerar relatório: ${error.message}`);
        
        // Volta para o passo de inspeção se for erro de preenchimento
        if (error.message.includes('Preencha o item de inspeção')) {
            showStep(4);
        }
    } finally {
        loading.classList.remove('active');
    }
}

// Inicialização
document.addEventListener('DOMContentLoaded', () => {
    updateProgress();
    
    // Configura data atual como padrão
    const today = new Date().toISOString().split('T')[0];
    document.getElementById('data').value = today;
});