const currencySelect = document.getElementById('currency-select');
const totalPrice = document.getElementById('total-price');


function loadRates() {
    fetch('https://openexchangerates.org/api/latest.json?app_id=b1f712f1977e4b1eb802159f2974c461')
        .then(response => response.json())
        .then(data => fillCurrencySelect(data.rates)).catch(Error);
}


function fillCurrencySelect(rates) {
    for (const rate in rates) {
        const option = document.createElement('option');
        option.value = rates[rate];
        option.text = rate;
        currencySelect.appendChild(option);
    }
}

function updateTotalPrice(rate) {
    const price = parseFloat(totalPrice.innerText);
    console.log(price)
    const newPrice = price * rate;
    totalPrice.innerText = newPrice.toFixed(2);
}

currencySelect.addEventListener('change', function () {
    const selectedRate = this.value;
    console.log(selectedRate)
    updateTotalPrice(selectedRate);
});

loadRates();