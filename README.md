### Cauldron Overhaul
An extension to vanilla cauldron mechanics to allow for arbitrary fluid counts and fluids other than water.

To add custom actions to the cauldrons, create an action class extending ICauldronAction, and initialize it using CauldronActions.addAction

```
CauldronActionExamlpe implements ICauldronAction {
    public ActionResult onUse(CauldronBlockEntity entity, World world, BlockPos pos, PlayerEntity player, Hand hand){
        return ActionResult.SUCCESS; //Successful action
        return ActionResult.PASS;   //Failed action
    }
}

...

(in common initializer)
CauldronActions.addAction(new CauldronActionExample());
```

Eventually, multiblock cauldron structures and brewing will be implemented. Right now, cauldrons have close to default behavior.