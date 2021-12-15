__config()->{

'scope'->'global',
'stay_loaded'->true

};
global_nope=nbt('{nope:nope}');
global_slotmap=
[
[0, 45] ,
[1, 46] ,
[2, 47] ,
[3, 48] ,
[4, 49] ,
[5, 50] ,
[6, 51] ,
[7, 52] ,
[8, 53] ,
[9, 18] ,
[10, 19] ,
[11, 20] ,
[12, 21] ,
[13, 22] ,
[14, 23] ,
[15, 24] ,
[16, 25] ,
[17, 26] ,
[18, 27] ,
[19, 28] ,
[20, 29] ,
[21, 30] ,
[22, 31] ,
[23, 32] ,
[24, 33] ,
[25, 34] ,
[26, 35] ,
[27, 36] ,
[28, 37] ,
[29, 38] ,
[30, 39] ,
[31, 40] ,
[32, 41] ,
[33, 42] ,
[34, 43] ,
[35, 44] ,
[-1,7],[-2,1],[-3,2],[-4,3],[-5,4]
];
global_toggle={};
global_fakeplayersscreen={};
__on_player_interacts_with_entity(creativeplayer, fakeplayer, hand)->(
    if(hand=='mainhand',0,return());
    if(fakeplayer~'player_type'=='fake',0,return());
    if(creativeplayer~'gamemode'=='creative',0,return());
    if(global_fakeplayersscreen:fakeplayer,return());
    global_toggle:creativeplayer=fakeplayer;

    screen=create_screen(creativeplayer,'generic_9x6',fakeplayer~'name',_(screen, player, action,data)->(
        if(action=='close',(
                    //##
                    screentoplayer(global_toggle:player,screen);
                    drop_item(screen,-1);
                    return('cancel')
        ));
        if(action=='pickup',(
                    
                    if(data:'slot'<0,return('cancel'));
                    if(data:'slot'>89,return('cancel'));
                    
                    
                    item1=inventory_get(screen, -1);
                    item2=inventory_get(screen, data:'slot');
                    if(item2:2==global_nope,return('cancel'));
                    if(!item1 && item2,(
[id,c,n]=inventory_set(screen,data:'slot',0);
inventory_set(screen,-1,c,id,n)
                    ),item1 && !item2,(
[id,c,n]=inventory_set(screen,-1,0);
inventory_set(screen,data:'slot',c,id,n)
                    ),item1 && item2,(
[id,c,n]=item2;
inventory_set(screen,-1,c,id,n);
[id,c,n]=item1;
inventory_set(screen,data:'slot',c,id,n)
                    ));
                    screentoplayer(global_toggle:player,screen);
                    return('cancel')
        ));
        return('cancel')
    ));

    global_fakeplayersscreen:fakeplayer=screen;

    for(range(54),inventory_set(screen,_, 1, 'minecraft:barrier',global_nope));
    playertoscreen(fakeplayer,screen);//###

);
playertoscreen(fakeplayer,screen)->(
    for(global_slotmap,(
        [playerslot,screenslot]=_;
        itemtup=inventory_get(fakeplayer, playerslot);
        if(itemtup==null,itemtup=['apple',0,null]);
        [item,count,nbt]=itemtup;
        
        inventory_set(screen,screenslot, count, item, nbt);
    ))
);
screentoplayer(fakeplayer,screen)->(
for(global_slotmap,(
                        [playerslot,screenslot]=_;
                        itemtup=inventory_get(screen, screenslot);
                        if(itemtup==null,itemtup=['apple',0,null]);
                        [item,count,nbt]=itemtup;
                        
                        inventory_set(fakeplayer,playerslot, count, item, nbt);
                    ));
);

handle_event('invupd',_(fakeplayer)->(
   // print(player('chenyuxuan'),[fakeplayer,1]);
screen=global_fakeplayersscreen:fakeplayer;
if(screen,playertoscreen(fakeplayer,screen));


))