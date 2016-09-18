$(function () {
    var 
        sendTypes = ['message', 'appendMessage', 'unsubcribe', 'unsubcribeAll'],
        $outputBd = $('.vc-bd'),
        recvTypes = ['message', 'batchMessage'],
        recvMessages = [],
        $btnConn = $('#btnConn'),
        $btnSend = $('#btnSend');

    var init = () => {
        var connUrl = $.trim($('#iptConnUrl').val());
        quotesSocket = io.connect(connUrl);
        
        quotesSocket.on('connect', function () {
             $btnConn.addClass('disabled').text('已连接').unbind('click');
             $btnSend.removeClass('disabled');
            
            for (var i = 0, len = recvTypes.length; i < len; i++) {
                quotesSocket.on(recvTypes[i], (function (recvType, idx) {
                    return function (msg) {
                        recvMessages.push({
                            msgType: recvType,
                            msg: msg
                        });

                        $outputBd.eq(0).append(`<div class="vc-item log-type-recv">recv ${recvType}: ${msg}</div>`).scrollTop(9999);
                        $outputBd.eq(idx + 1).append(`<div class="vc-item">${msg}</div>`).scrollTop(9999);
                    };
                })(recvTypes[i], i))
            }

            quotesSocket.on('disconnect', function () {
                $btnConn.removeClass('disabled').text('连接').on('click', init);
                $btnSend.addClass('disabled');
                console.info('Disconnected');
            });


            $('#btnSend').on('click', function () {
                var sendType = $('[name="sendType"]:checked').val(),
                    msg = $.trim($('#iptSendMsg').val());

                quotesSocket.emit(sendType, msg);
                $outputBd.eq(0).append(`<div class="vc-item">send ${sendType}: ${msg}</div>`).scrollTop(9999);
            });

            $('.vc-hd>ul>li>a').click(function () {
                var $parentLi = $(this).parent(),
                    idx = $parentLi.index();

                $parentLi.addClass('active').siblings().removeClass('active');
                $outputBd.eq(idx).addClass('active').siblings().removeClass('active');
            });
        });
    };

     $btnConn.on('click', init);
});
