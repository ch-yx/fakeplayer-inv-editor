__config()->{
    'scope'->'global',
    'stay_loaded'->true
};

global_nope=nbt('{nope:nope}');

global_slotmap=[[-1,7],[-2,1],[-3,2],[-4,3],[-5,4],...map(range(9),[_,45+_]),...map(range(27),[9+_,18+_])];


global_fakeplayersscreen={};

__on_player_interacts_with_entity(creativeplayer, fakeplayer, hand)->(
    if(hand=='mainhand',0,return());
    if(fakeplayer~'player_type'=='fake',0,return());
    //if(creativeplayer~'gamemode'=='creative',0,return());
    if(global_fakeplayersscreen:fakeplayer,return());

    

    screen=create_screen(creativeplayer,'generic_9x6',fakeplayer~'name',_(screen, player, action,data,outer(fakeplayer))->(
        if(action=='close',(
                    screentoplayer(fakeplayer,screen);
                    drop_item(screen,-1);
                    close_screen(screen);//end_portal/die
                    return('cancel')
        ));
        if(action=='pickup',(
                    
                    if(data:'slot'<0,return('cancel'));
                    if(data:'slot'>89,return('cancel'));
                    
                    item1=inventory_get(screen, -1);
                    item2=inventory_get(screen, data:'slot');

                    if(item2:2==global_nope,(
                        if(data:'slot'<9,return('cancel'));
                        if(data:'slot'>17,return('cancel'));
                        //modify(fakeplayer, 'selected_slot', data:'slot'-9);
                        //BUG!!!
                        run('player '+fakeplayer~'command_name'+' hotbar '+(data:'slot'-8));
                        return('cancel')
                    ));
                    
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
                    screentoplayer(fakeplayer,screen);
                    return('cancel')
        ));
        return('cancel')
    ));

    global_fakeplayersscreen:fakeplayer=screen;

    loop(54,inventory_set(screen,_, 1, 'minecraft:structure_void',global_nope));
    inventory_set(screen,fakeplayer~'selected_slot'+9, 1, 'minecraft:barrier',global_nope);
    playertoscreen(fakeplayer,screen);

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
    screen=global_fakeplayersscreen:fakeplayer;
    if(screen,playertoscreen(fakeplayer,screen));
));


__on_player_switches_slot(fakeplayer, from, to)->(
    screen=global_fakeplayersscreen:fakeplayer;
    if(screen,(
        inventory_set(screen,from+9, 1, 'minecraft:structure_void',global_nope);
        inventory_set(screen,to  +9, 1, 'minecraft:barrier',global_nope);
    ))
);


__on_player_disconnects(fakeplayer, reason)->(
    screen=global_fakeplayersscreen:fakeplayer;
    if(screen,(
        drop_item(screen,-1);
        close_screen(screen);
    ))
)