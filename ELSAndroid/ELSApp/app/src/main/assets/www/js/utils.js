function toDate(longDate){
    var now = new Date;
    console.log( now.customFormat( "#DD#/#MM#/#YYYY# #hh#:#mm#:#ss#" ) );
}

function sleep(ms) {
  return new Promise(resolve => setTimeout(resolve, ms));
}

