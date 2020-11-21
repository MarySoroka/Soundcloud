<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<jsp:directive.attribute name="subscription" type="com.soundcloud.subscription.Subscription" required="true"
                         description="subscription"/>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<div class="col-50">
    <h5><fmt:message key="profile.payment.refill.title"/></h5>
    <span><fmt:message key="profile.payment.refill.text"/></span><br><br>
    <label><fmt:message key="profile.payment.cards"/></label>
    <div class="icon-container">
        <i class="fa fa-cc-visa" style="color:navy;"></i>
        <i class="fa fa-cc-amex" style="color:blue;"></i>
        <i class="fa fa-cc-mastercard" style="color:red;"></i>
        <i class="fa fa-cc-discover" style="color:orange;"></i>
    </div>
    <div class="row">
        <div class="input-field col s12">
            <input type="text" id="cname" name="cardname white-text" placeholder="John More" required>
            <label for="cname"><fmt:message key="profile.payment.card.owner"/></label>
        </div>
    </div>
    <div class="row">
        <div class="input-field col s12">
            <input maxlength="16" minlength="16" type="text" id="card-number" name="card-number white-text"
                   placeholder="1111222233334444" required>
            <label for="card-number"><fmt:message key="profile.payment.card.number"/></label>

        </div>
    </div>
    <div class="row">
        <div class="input-field col s12">
            <p class="grey-text"><fmt:message key="profile.payment.expire.month"/></p>
            <input placeholder="2015-01" id="date-demo1" type="month" class="grey darken-2" required>
        </div>
    </div>
    <div class="row">
        <div class="input-field col s12">
            <input type="text" maxlength="3" minlength="3" id="cvv" name="cvv" placeholder="352" class="white-text"
                   required>
            <label for="cvv"><fmt:message key="profile.payment.card.cvv"/></label>

        </div>
    </div>
    <div class="row">
        <div class="input-field col s12">
            <input type="text" id="walletAmount" maxlength="5" name="walletAmount" placeholder="10" class="white-text"
                   required><c:out
                value="$"/>
            <label for="walletAmount"><fmt:message key="profile.wallet.amount"/></label>

        </div>
    </div>

</div>
<script type="text/javascript" src="https://github.com/jaridmargolin/formatter.js"></script>
<script src="static/js/validation.js"></script>
<script type="text/javascript">
    setInputFilter(document.getElementById('cvv'), function (value) {
        return /^\d*?\d*$/.test(value);
    });
    setInputFilter(document.getElementById('card-number'), function (value) {
        return /^\d*?\d*$/.test(value);
    });
    setInputFilter(document.getElementById('walletAmount'), function (value) {
        return /^\d*?\d*$/.test(value);
    });
</script>