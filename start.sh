#emacs stars.sc &
(sleep 4 ; jack_connect SuperCollider:out_2 system:playback_1 ) &
(sleep 14 ; jack_connect SuperCollider:out_2 system:playback_1 ) &
(sleep 36 ; jack_connect SuperCollider:out_2 system:playback_1 ) &
(sleep 120 ; jack_connect SuperCollider:out_2 system:playback_1 ) &
(sleep 4 ; firefox http://127.0.0.1:3000/menu.html ) &
perl oscrelay.pl daemon &
sclang stars.sc 
#(sleep 4 ; firefox http://127.0.0.1:3000/menu.html ) &
#perl oscrelay.pl daemon
echo killing the webservice
pgrep -f oscrelay.pl | xargs kill

