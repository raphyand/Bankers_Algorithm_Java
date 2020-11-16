public class Factory_Initializer_Helper {

    int[] allocated;
    int[] maxDemand;
    int[] inputNumbers;
    int placementInArray;


    protected void initializeMaxDemandAndAllocation(int[] allocated, int[] maxDemand, int[] inputNumbers, int placementInArray){
        for (int i = 0; i < allocated.length; i++){
            allocated[i] = inputNumbers[placementInArray];
            placementInArray++;
        }
        for (int i = 0; i < maxDemand.length; i++){
            maxDemand[i] = inputNumbers[placementInArray];
            placementInArray++;
        }
    }

    protected void updateFactoryPlacementInArray(int factoryPlacement){
        factoryPlacement = placementInArray;
    }

    public Factory_Initializer_Helper(int[] allocated, int[] maxDemand, int[] inputNumbers, int placementInArray){
        this.allocated = allocated;
        this.maxDemand = maxDemand;
        this.inputNumbers = inputNumbers;
        this.placementInArray = placementInArray;

    }


}
