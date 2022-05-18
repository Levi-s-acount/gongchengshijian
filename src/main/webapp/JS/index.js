window.addEventListener('DOMContentLoaded',function(){
    // 考试和练习选择交互功能
    const itp=document.querySelector('.initpage');
    itp.children[0].innerHTML='欢迎'+sessionStorage.getItem('account')+'回来，边卷边摆效率更高';
    const docEl=document.documentElement;
    const mc=document.querySelector('.moving-cat');
    let length=0;
    let flag=0;
    // console.log(box.offsetLeft);
    let timer=setInterval(function(){
        if(flag===0){
            if(mc.offsetLeft<docEl.clientWidth-600){
                length++;
                mc.style.left=length+'px';
                // console.log(box.offsetLeft);
            }else {
                // console.log(box.offsetLeft);
                // length=-600;
                // mc.style.left=length+'px';
                flag=1;
            }
        }else if(flag===1){
            if(mc.offsetLeft>0){
                length--;
                mc.style.left=length+'px';
            }else{
                flag=0;
            }
        }
    },1) 
})