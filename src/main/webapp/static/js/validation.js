function validate(inputId) {
    validateField(inputId);
}

function isPasswordSame() {
    if (document.getElementById('password').value ===
        document.getElementById('password-repeat').value) {
        document.getElementById('password-repeat').classList.remove('invalid');
        document.getElementById('password-repeat').classList.add('valid');
    } else if (document.getElementById('password').value !==
        document.getElementById('password-repeat').value) {
        document.getElementById('password-repeat').classList.remove('valid');
        document.getElementById('password-repeat').classList.add('invalid');
    }

}

function validateLength(inputId) {
  if (document.getElementById(inputId).value.length<3 ||document.getElementById(inputId).value.length>30){
      document.getElementById(inputId).classList.remove('valid');
      document.getElementById(inputId).classList.add('invalid');
  }else {
      document.getElementById(inputId).classList.remove('invalid');
      document.getElementById(inputId).classList.add('valid');
  }

}
function validateCurrentLength(inputId, length) {
    if (document.getElementById(inputId).value.length!==length){
        document.getElementById(inputId).classList.remove('valid');
        document.getElementById(inputId).classList.add('invalid');
    }else {
        document.getElementById(inputId).classList.remove('invalid');
        document.getElementById(inputId).classList.add('valid');
    }

}
function validatePattern(inputId, pattern) {
    if (!document.getElementById(inputId).value.matchAll(pattern)){
        document.getElementById(inputId).classList.remove('valid');
        document.getElementById(inputId).classList.add('invalid');
    }else {
        document.getElementById(inputId).classList.remove('invalid');
        document.getElementById(inputId).classList.add('valid');
    }

}
function setInputFilter(textbox, inputFilter) {
    ["input", "keydown", "keyup", "mousedown", "mouseup", "select", "contextmenu", "drop"].forEach(function(event) {
        textbox.addEventListener(event, function() {
            if (inputFilter(this.value)) {
                this.oldValue = this.value;
                this.oldSelectionStart = this.selectionStart;
                this.oldSelectionEnd = this.selectionEnd;
            } else if (this.hasOwnProperty("oldValue")) {
                this.value = this.oldValue;
                this.setSelectionRange(this.oldSelectionStart, this.oldSelectionEnd);
            } else {
                this.value = "";
            }
        });
    });
}
