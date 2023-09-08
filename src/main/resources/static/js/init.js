$(document).ready(function () {
var value = localStorage.getItem("Authorization");
if(value != null) {
$.ajaxSetup({
headers: { 'Authorization':  value }
});
}
})