webpackJsonp([24],{DgPo:function(t,a,e){"use strict";Object.defineProperty(a,"__esModule",{value:!0});var s=e("3cXf"),n=e.n(s),o=e("HzJ8"),i=e.n(o),r=e("LPk9"),c=e("6ROu"),l=e.n(c),u=e("x47x"),v=e("YgNb"),d=(e("2tLR"),{data:function(){return{hash:this.$route.query.hash,infoData:[],inputs:[],allInputs:0,outputs:[],allOutputs:0,times:"",contractIf:!1,callIf:!1,insideIf:!1,tokenIf:!1}},components:{Back:r.a},mounted:function(){this.getHashInfo(this.hash)},methods:{getHashInfo:function(t){var a=this,e="",s=document.getElementById("outPre");e=100===this.$route.query.type||101===this.$route.query.type||102===this.$route.query.type||103===this.$route.query.type||1e3===this.$route.query.type?"/contract/tx/"+t:"/accountledger/tx/"+t,this.$fetch(e).then(function(t){if(a.infoData=t.data,a.infoData.fee=Object(v.b)(t.data.fee).toString(),a.times=l()(Object(v.i)(t.data.time)).format("YYYY-MM-DD HH:mm:ss"),t.data.remark=t.data.remark?Object(v.j)(t.data.remark):"",t.data.inputs.length>0)for(var e=0;e<t.data.inputs.length;e++)t.data.inputs[e].value=Object(v.b)(t.data.inputs[e].value),a.allInputs=Object(u.BigNumber)(a.allInputs).plus(t.data.inputs[e].value).toString();if(a.inputs=t.data.inputs,t.data.outputs.length>0)for(var o=0;o<t.data.outputs.length;o++)t.data.outputs[o].value=Object(v.b)(t.data.outputs[o].value),a.allOutputs=Object(u.BigNumber)(a.allOutputs).plus(t.data.outputs[o].value).toString();if(a.outputs=t.data.outputs,100===t.data.type||101===t.data.type||102===t.data.type||1e3===t.data.type){if(a.contractIf=!0,100===t.data.type);else if(101===t.data.type){if(a.callIf=!0,t.data.callName=t.data.txData.data.methodName,t.data.callParmas="",t.data.txData.data.args.length>0){var r=!0,c=!1,d=void 0;try{for(var _,f=i()(t.data.txData.data.args);!(r=(_=f.next()).done);r=!0){var m=_.value;t.data.callParmas+=m[0]+","}}catch(t){c=!0,d=t}finally{try{!r&&f.return&&f.return()}finally{if(c)throw d}}t.data.callParmas.length>0&&(t.data.callParmas=t.data.callParmas.substr(0,t.data.callParmas.length-1))}if(t.data.callResult=t.data.contractResult.result,t.data.contractResult.transfers.length>0){a.insideIf=!0,t.data.insideUnit="NULS";var p=t.data.contractResult.transfers;for(var h in p)"0"===h.toString()?p[h].name=a.$t("message.c222"):p[h].name="",p[h].value=Object(v.b)(p[h].value).toString();t.data.insideItme=t.data.contractResult.transfers}if(t.data.contractResult.tokenTransfers.length>0){a.tokenIf=!0,t.data.tokenUnit=t.data.contractResult.symbol;var g=Object(v.d)(t.data.contractResult.decimals),w=t.data.contractResult.tokenTransfers;for(var I in w)"0"===I.toString()?w[I].name="Token":w[I].name="",w[I].value=Object(v.a)(w[I].value,g).toString();t.data.tokenItme=t.data.contractResult.tokenTransfers}}else a.contractIf=!1,t.data.txDataHexString="",t.data.contractAddress=t.data.contractResult.contractAddress;t.data.totalFee=Object(v.b)(t.data.contractResult.totalFee).toString(),t.data.txSizeFee=Object(v.b)(t.data.contractResult.txSizeFee).toString(),t.data.actualContractFee=Object(v.b)(t.data.contractResult.actualContractFee).toString(),t.data.refundFee=Object(v.b)(t.data.contractResult.refundFee).toString(),t.data.contractAddress=t.data.contractResult.contractAddress,t.data.gasLimit=t.data.contractResult.gasLimit,t.data.price=t.data.contractResult.price,t.data.gasUsed=t.data.contractResult.gasUsed,t.data.modelIf=t.data.contractResult.success?"true":t.data.contractResult.success+"("+t.data.contractResult.errorMessage+")",s.innerText=n()(t.data.txData.data,null,2)}})},hashCopy:function(t){t.length>50?window.open("https://nulscan.io/transactionHash?hash="+t,"_blank"):window.open("https://nulscan.io/accountInfo?address="+t,"_blank")}},beforeRouteLeave:function(t,a,e){"/wallet"!==t.name&&(sessionStorage.removeItem("walletTotalAll"),sessionStorage.removeItem("walletPages"),sessionStorage.removeItem("walletTypes")),e()}}),_={render:function(){var t=this,a=t.$createElement,e=t._self._c||a;return e("div",{staticClass:"deal-info"},[e("Back",{attrs:{backTitle:this.$t("message.transactionManagement")}}),t._v(" "),e("div",{staticClass:"deal-info-top"},[e("div",{staticClass:"deal-left fl"},[e("div",[t._v(t._s(t.$t("message.input"))),e("span",[t._v(" "+t._s(this.allInputs.toString())+" NULS")])]),t._v(" "),e("ul",t._l(t.inputs,function(a){return e("li",[e("label",{staticClass:"cursor-p",on:{click:function(e){t.hashCopy(a.address)}}},[t._v(t._s(a.address))]),t._v(" "),e("span",[t._v(t._s(a.value.toString()))])])}))]),t._v(" "),e("div",{staticClass:"deal-right fr"},[e("div",[t._v(t._s(t.$t("message.output"))),e("span",[t._v(t._s(this.allOutputs.toString())+" NULS")])]),t._v(" "),e("ul",t._l(t.outputs,function(a){return e("li",[e("label",{staticClass:"cursor-p",on:{click:function(e){t.hashCopy(a.address)}}},[t._v(t._s(a.address))]),t._v(" "),e("span",[t._v(t._s(a.value.toString()))])])}))])]),t._v(" "),e("div",{staticClass:"deal-case"},[e("h3",[t._v(t._s(t.$t("message.overview")))]),t._v(" "),e("ul",[e("li",[e("span",[t._v(t._s(t.$t("message.tradingTime")))]),t._v(t._s(this.times))]),t._v(" "),e("li",{directives:[{name:"show",rawName:"v-show",value:!t.contractIf,expression:"!contractIf"}]},[e("span",[t._v(t._s(t.$t("message.miningFee1")))]),t._v(t._s(t.infoData.fee)+" NULS")]),t._v(" "),e("li",{directives:[{name:"show",rawName:"v-show",value:t.contractIf,expression:"contractIf"}]},[e("span",[t._v(t._s(t.$t("message.miningFee1")))]),t._v("\n        "+t._s(this.infoData.totalFee)+"("+t._s(t.$t("message.c210"))+") =\n        "+t._s(this.infoData.txSizeFee)+"（"+t._s(t.$t("message.c211"))+"）+\n        "+t._s(this.infoData.actualContractFee)+"（"+t._s(t.$t("message.type"+t.infoData.type))+"）+\n        "+t._s(this.infoData.refundFee)+"（"+t._s(t.$t("message.c213"))+"）\n        "),e("label",{staticClass:"unit"},[t._v(t._s(t.$t("message.c214"))+": NULS")])]),t._v(" "),e("li",[e("span",[t._v(t._s(t.$t("message.autograph")))]),e("label",{staticClass:"text-ds cursor-p",on:{click:function(a){t.hashCopy(t.infoData.hash)}}},[t._v(t._s(t.infoData.hash))])]),t._v(" "),e("li",[e("span",[t._v(t._s(t.$t("message.transactionType")))]),t._v(t._s(t.$t("message.type"+t.infoData.type)))]),t._v(" "),e("li",[e("span",[t._v(t._s(t.$t("message.transactionState")))]),t._v(t._s(t.$t("message.statusS"+t.infoData.status)))]),t._v(" "),e("li",{directives:[{name:"show",rawName:"v-show",value:t.contractIf||102===t.infoData.type,expression:"contractIf || infoData.type === 102 "}]},[e("span",[t._v(t._s(t.$t("message.c215")))]),t._v(t._s(t.infoData.contractAddress))]),t._v(" "),e("li",{directives:[{name:"show",rawName:"v-show",value:t.contractIf||102===t.infoData.type,expression:"contractIf || infoData.type === 102 "}]},[e("span",[t._v(t._s(t.$t("message.c247")))]),e("font",{class:t.infoData.modelIf?"success":"failed"},[t._v(t._s(t.$t("message.c"+t.infoData.modelIf)))])],1),t._v(" "),t._l(t.infoData.insideItme,function(a){return e("li",{directives:[{name:"show",rawName:"v-show",value:t.insideIf,expression:"insideIf"}]},[e("span",[t._v(t._s(""===a.name?" ":a.name))]),t._v("\n        From "),e("label",{staticClass:"text-ds cursor-p",on:{click:function(e){t.hashCopy(a.from)}}},[t._v(t._s(a.from))]),t._v("\n        To "),e("label",{staticClass:"text-ds cursor-p",on:{click:function(e){t.hashCopy(a.to)}}},[t._v(t._s(a.to))]),t._v("\n        for "),e("label",{staticClass:"text-ds"},[t._v(t._s(a.value))]),t._v(" "+t._s(t.infoData.insideUnit)+"\n        "),e("p",[e("span",[t._v(" ")]),t._v("TXID: "),e("label",{staticClass:"text-ds cursor-p",on:{click:function(e){t.hashCopy(a.to)}}},[t._v(t._s(a.txHash))])])])}),t._v(" "),t._l(t.infoData.tokenItme,function(a){return e("li",{directives:[{name:"show",rawName:"v-show",value:t.tokenIf,expression:"tokenIf"}]},[e("span",[t._v(t._s(a.name)+" ")]),t._v("\n        From "),e("label",{staticClass:"text-ds cursor-p",on:{click:function(e){t.hashCopy(a.from)}}},[t._v(t._s(a.from))]),t._v("\n        To "),e("label",{staticClass:"text-ds cursor-p",on:{click:function(e){t.hashCopy(a.to)}}},[t._v(t._s(a.to))]),t._v("\n        for "),e("label",{staticClass:"text-ds"},[t._v(t._s(a.value))]),t._v(" "+t._s(t.infoData.tokenUnit)+"\n      ")])}),t._v(" "),e("li",{directives:[{name:"show",rawName:"v-show",value:t.contractIf,expression:"contractIf"}]},[e("span",[t._v("GasLimit")]),t._v(t._s(t.infoData.gasLimit))]),t._v(" "),e("li",{directives:[{name:"show",rawName:"v-show",value:t.contractIf,expression:"contractIf"}]},[e("span",[t._v(t._s(t.$t("message.c216")))]),t._v(t._s(t.infoData.price))]),t._v(" "),e("li",{directives:[{name:"show",rawName:"v-show",value:t.contractIf,expression:"contractIf"}]},[e("span",[t._v("GasUsed")]),t._v(t._s(t.infoData.gasUsed))]),t._v(" "),e("li",{directives:[{name:"show",rawName:"v-show",value:t.callIf,expression:"callIf"}]},[e("span",[t._v(t._s(t.$t("message.c217")))]),t._v(t._s(t.$t("message.c218"))+": "+t._s(t.infoData.callName))]),t._v(" "),e("li",{directives:[{name:"show",rawName:"v-show",value:t.callIf,expression:"callIf"}]},[e("span",[t._v(" ")]),t._v(t._s(t.$t("message.c219"))+": "+t._s(t.infoData.callParmas))]),t._v(" "),e("li",{directives:[{name:"show",rawName:"v-show",value:t.callIf,expression:"callIf"}]},[e("span",[t._v(" ")]),t._v(t._s(t.$t("message.c2201"))+": "+t._s(t.infoData.callResult))]),t._v(" "),e("li",[e("span",[t._v(t._s(t.$t("message.blockHeight")))]),t._v(t._s(t.infoData.blockHeight<0?"- -":t.infoData.blockHeight))]),t._v(" "),e("li",[e("span",[t._v(t._s(t.$t("message.c221")))]),t._v(t._s(t.infoData.confirmCount))]),t._v(" "),e("li",{directives:[{name:"show",rawName:"v-show",value:102!==t.infoData.type,expression:"infoData.type !== 102"}]},[e("span",[t._v("TxData")]),t._v(" "),e("pre",{directives:[{name:"show",rawName:"v-show",value:!t.contractIf,expression:"!contractIf"}],staticClass:"out-pre"},[t._v(t._s(""===t.infoData.txDataHexString?" ":t.infoData.txDataHexString))]),t._v(" "),e("pre",{directives:[{name:"show",rawName:"v-show",value:t.contractIf,expression:"contractIf"}],staticClass:"out-pre",attrs:{id:"outPre"}})]),t._v(" "),e("li",[e("span",[t._v(t._s(t.$t("message.remarks")))]),t._v(t._s(""===t.infoData.remark?" ":t.infoData.remark))])],2)])],1)},staticRenderFns:[]};var f=e("vSla")(d,_,!1,function(t){e("MoDJ")},null,null);a.default=f.exports},MoDJ:function(t,a){}});