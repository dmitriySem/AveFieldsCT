// STAR-CCM+ macro: macros1.java
// Written by STAR-CCM+ 15.02.007
package macro;

import java.util.*;
import java.lang.StringBuilder;
import java.io.File;

import star.common.*;
import star.base.neo.*;
import star.base.report.*;
import star.energy.*;
import star.vis.*;


public class macros1 extends StarMacro {

  public void execute() {
    execute0();
  }

  private void execute0() {
      

      
    boolean flagVallOrRK3 = true; // True is Vall; Falce is RK3
    boolean flagTemOrHTC = true; //True is temperature; Falce is HTC
    for (int g = 1; g <= 4; g++) {
        if (g == 1) {
            flagVallOrRK3 = true;
            flagTemOrHTC = true;
        }
        if (g == 2) {
            flagVallOrRK3 = true;
            flagTemOrHTC = false;
        }
        if (g == 3) {
            flagVallOrRK3 = false;
            flagTemOrHTC = true;
        }
        if (g == 4) {
            flagVallOrRK3 = false;
            flagTemOrHTC = false;
        }
        
    /*//double Lmax = -0.0016274158889413716; // Right boundary of Vall
    double Lmax = -0.0015; // Right boundary of Vall
    //double Lmin = -0.0016274158889413716 - 0.0145; // Left boundary of Vall
    //double Lmin = -0.0015 - 0.0145; // Left boundary of Vall
    
    //double Lmin = -0.10655262789274132; // Left boundary of Vall
    //0.012179939960092711, 0.06838516105998035*/
    double Lmax, Lmin;
    
    if (flagVallOrRK3) {
        Lmax = -0.0015; // Right boundary of Vall
        Lmin = -0.0235; // Left boundary of Vall          
    } else {
        Lmax = 0.0115; // Right boundary of RK3
        Lmin = -0.0016;// Left boundary of RK3
    }
    
    int nx = 10; // Number of thresholds along axises x
    int nr = 10; // Number of thresholds along axises r
    
    Simulation simulation_0 = 
      getActiveSimulation();

    Units units_2 = 
      ((Units) simulation_0.getUnitsManager().getObject("m"));
    Region region_0 = 
      simulation_0.getRegionManager().getRegion("Air-2_Rotated");
    
    InterfaceBoundary interfaceBoundary_0; 
      if (flagVallOrRK3) {
        interfaceBoundary_0 =
          ((InterfaceBoundary) region_0.getBoundaryManager().getBoundary("int-10 Air-2/Vall CT_Contact [Interface 18]"));
      } else {
        interfaceBoundary_0 = 
          ((InterfaceBoundary) region_0.getBoundaryManager().getBoundary("int-9 Air-2/RK-III-Solid_Contact [Interface 44]"));
      }
    
    /*PrimitiveFieldFunction primitiveFieldFunction_1 = 
      ((PrimitiveFieldFunction) simulation_0.getFieldFunctionManager().getFunction("Position"));
    VectorComponentFieldFunction vectorComponentFieldFunction_0 = 
      ((VectorComponentFieldFunction) primitiveFieldFunction_1.getComponentFunction(0));*/
    
    
    
    PrimitiveFieldFunction primitiveFieldFunction_1 = 
      ((PrimitiveFieldFunction) simulation_0.getFieldFunctionManager().getFunction("Position"));
    
    LabCoordinateSystem labCoordinateSystem_0 = 
      simulation_0.getCoordinateSystemManager().getLabCoordinateSystem();
    
    CylindricalCoordinateSystem cylindricalCoordinateSystem_0 = 
      labCoordinateSystem_0.getLocalCoordinateSystemManager().createLocalCoordinateSystem(CylindricalCoordinateSystem.class, "Cylindrical");
    cylindricalCoordinateSystem_0.setBasis0(new DoubleVector(new double[] {0.0, 1.0, 0.0}));
    cylindricalCoordinateSystem_0.setBasis1(new DoubleVector(new double[] {0.0, 0.0, 1.0}));
    
    /*CylindricalCoordinateSystem cylindricalCoordinateSystem_0 = 
      ((CylindricalCoordinateSystem) labCoordinateSystem_0.getLocalCoordinateSystemManager().getObject("Cylindrical 1"));*/
    
    VectorComponentFieldFunction vectorComponentFieldFunction_Z = 
      ((VectorComponentFieldFunction) primitiveFieldFunction_1.getFunctionInCoordinateSystem(cylindricalCoordinateSystem_0).getComponentFunction(2));
    VectorComponentFieldFunction vectorComponentFieldFunction_R = 
      ((VectorComponentFieldFunction) primitiveFieldFunction_1.getFunctionInCoordinateSystem(cylindricalCoordinateSystem_0).getComponentFunction(0));
      
    PrimitiveFieldFunction primitiveFieldFunction_0;
      if (flagTemOrHTC) {
        primitiveFieldFunction_0 = 
            ((PrimitiveFieldFunction) simulation_0.getFieldFunctionManager().getFunction("HeatTransferReferenceTemperatureUserYPlus"));
      } else {
        primitiveFieldFunction_0 = 
            ((PrimitiveFieldFunction) simulation_0.getFieldFunctionManager().getFunction("HeatTransferCoefficientUserYPlus"));
      }
    /*HtcUserYPlusFunction htcUserYPlusFunction_0 = 
      ((HtcUserYPlusFunction) simulation_0.getFieldFunctionManager().getFunction("HeatTransferCoefficientUserYPlus"));*/
    

    
    /*___________________Create Treshold___________________*/
   NullFieldFunction nullFieldFunction_0 = 
      ((NullFieldFunction) simulation_0.getFieldFunctionManager().getFunction("NullFieldFunction"));
    ThresholdPart thresholdPart_3 = 
      simulation_0.getPartManager().createThresholdPart(new NeoObjectVector(new Object[] {}), new DoubleVector(new double[] {0.0, 1.0}), units_2, nullFieldFunction_0, 0);
    thresholdPart_3.getInputParts().setQuery(null);
    thresholdPart_3.getInputParts().setObjects(interfaceBoundary_0);
    thresholdPart_3.setFieldFunction(vectorComponentFieldFunction_Z);
    thresholdPart_3.getRangeQuantities().setArray(new DoubleVector(new double[] {Lmin, Lmax}));

    /*___________________Create Tave___________________*/
    AreaAverageReport areaAverageReport_1 = 
      simulation_0.getReportManager().createReport(AreaAverageReport.class);
    areaAverageReport_1.setFieldFunction(primitiveFieldFunction_0);
    areaAverageReport_1.getParts().setQuery(null);
    areaAverageReport_1.getParts().setObjects(thresholdPart_3);
    Units units_0 = 
      ((Units) simulation_0.getUnitsManager().getObject("K"));
    areaAverageReport_1.setUnits(units_0);
    areaAverageReport_1.setPresentationName("Tave_Vall");
    
    /*___________________Create Sigma T___________________*/
    SurfaceStandardDeviationReport surfaceStandardDeviationReport_1 = 
      simulation_0.getReportManager().createReport(SurfaceStandardDeviationReport.class);
    surfaceStandardDeviationReport_1.setFieldFunction(primitiveFieldFunction_0);
    surfaceStandardDeviationReport_1.setUnits(units_0);
    surfaceStandardDeviationReport_1.getParts().setQuery(null);
    surfaceStandardDeviationReport_1.getParts().setObjects(thresholdPart_3);
    surfaceStandardDeviationReport_1.setPresentationName("Sigba T_Vall");

    /*_______________Create treshold without outliers Tave +/- 2Sigma_______________________*/
    ThresholdPart thresholdPart_4 = 
      simulation_0.getPartManager().createThresholdPart(new NeoObjectVector(new Object[] {}), new DoubleVector(new double[] {0.0, 1.0}), units_2, nullFieldFunction_0, 0);
    thresholdPart_4.getInputParts().setQuery(null);
    thresholdPart_4.getInputParts().setObjects(thresholdPart_3);
    thresholdPart_4.setFieldFunction(primitiveFieldFunction_0);    
    double TaveMinusSigma = areaAverageReport_1.getValue() - (surfaceStandardDeviationReport_1.getValue());
    double TavePlusSigma = areaAverageReport_1.getValue() + (surfaceStandardDeviationReport_1.getValue());
    thresholdPart_4.getRangeQuantities().setArray(new DoubleVector(new double[] {TaveMinusSigma, TavePlusSigma}));
    
    
    double deltaZ = (Lmax - Lmin)/nx;
    double LeftBoundarisZ = Lmax, RightBoundariesZ = Lmax;
    
    double LeftBoundarisR = 0;
    double RightBoundariesR = 0;
    double deltaR = 0;
    
    
    ThresholdPart [] massiveOfThresholZ = new ThresholdPart[nx+1]; /*Massiv of thresholds for temperature*/ 
    AreaAverageReport [] massiveReports = new AreaAverageReport[nx+1]; /*Massiv of reports for temperature*/
    ThresholdPart [] massiveOfThresholR = new ThresholdPart[nx*nr+1]; /*Massiv of thresholds for temperature*/ 
    UserFieldFunction [] massiveOfFieldFunction = new UserFieldFunction[nx*nr+1]; /*Massiv of UserFieldFunction for temperature*/ 
    
    UserFieldFunction userFieldFunction_Sum = 
      simulation_0.getFieldFunctionManager().createFieldFunction();
    userFieldFunction_Sum.getTypeOption().setSelected(FieldFunctionTypeOption.Type.SCALAR);
    userFieldFunction_Sum.setPresentationName("ModifHeatTransferTem_sum");
    userFieldFunction_Sum.setFunctionName("ModifHeatTransferTem_sum");
    
    if (flagTemOrHTC) {
        userFieldFunction_Sum.setDimensions(Dimensions.Builder().temperature(1).build());
    } else {
        userFieldFunction_Sum.setDimensions(Dimensions.Builder().power(1).length(-2).temperature(-1).build());
    }
    
    
    MaxReport maxReport_1 = 
      simulation_0.getReportManager().createReport(MaxReport.class);
    maxReport_1.setFieldFunction(vectorComponentFieldFunction_R);
    maxReport_1.getParts().setQuery(null);
    
    MinReport minReport_0 = 
      simulation_0.getReportManager().createReport(MinReport.class);
    minReport_0.setFieldFunction(vectorComponentFieldFunction_R);
    minReport_0.getParts().setQuery(null);
    
    SumReport sumReport_2 = 
      simulation_0.getReportManager().createReport(SumReport.class);
    PrimitiveFieldFunction primitiveFieldFunction_8 = 
      ((PrimitiveFieldFunction) simulation_0.getFieldFunctionManager().getFunction("Area"));
    VectorMagnitudeFieldFunction vectorMagnitudeFieldFunction_0 = 
      ((VectorMagnitudeFieldFunction) primitiveFieldFunction_8.getMagnitudeFunction());
    sumReport_2.setFieldFunction(vectorMagnitudeFieldFunction_0);
    sumReport_2.getParts().setQuery(null);
    
    ArrayList <UserFieldFunction> ListofFunctions = new ArrayList<>();

    for (int i=1; i<=nx;i++){    
        RightBoundariesZ = LeftBoundarisZ;
        LeftBoundarisZ = LeftBoundarisZ - deltaZ;
        
        massiveOfThresholZ[i] =  
            simulation_0.getPartManager().createThresholdPart(new NeoObjectVector(new Object[] {}), new DoubleVector(new double[] {0.0, 1.0}), units_2, nullFieldFunction_0, 0);
        massiveOfThresholZ[i].getInputParts().setQuery(null);
        massiveOfThresholZ[i].getInputParts().setObjects(thresholdPart_4);
        massiveOfThresholZ[i].setFieldFunction(vectorComponentFieldFunction_Z);        
        massiveOfThresholZ[i].getRangeQuantities().setArray(new DoubleVector(new double[] {LeftBoundarisZ, RightBoundariesZ}));
        
        maxReport_1.getParts().setObjects(massiveOfThresholZ[i]);
        minReport_0.getParts().setObjects(massiveOfThresholZ[i]);
        sumReport_2.getParts().setObjects(massiveOfThresholZ[i]);
        deltaR = (maxReport_1.getValue() - minReport_0.getValue())/nr;
        LeftBoundarisR = maxReport_1.getValue();
        
        for (int j = 1; j <= nr; j++) {
            StringBuilder FieldFunctionBuilder = new StringBuilder();
            RightBoundariesR = LeftBoundarisR;
            LeftBoundarisR = LeftBoundarisR - deltaR;
        
            massiveOfThresholR[j] =  
            simulation_0.getPartManager().createThresholdPart(new NeoObjectVector(new Object[] {}), new DoubleVector(new double[] {0.0, 1.0}), units_2, nullFieldFunction_0, 0);
            massiveOfThresholR[j].getInputParts().setQuery(null);
            massiveOfThresholR[j].getInputParts().setObjects(massiveOfThresholZ[i]);
            massiveOfThresholR[j].setFieldFunction(vectorComponentFieldFunction_R);        
            massiveOfThresholR[j].getRangeQuantities().setArray(new DoubleVector(new double[] {LeftBoundarisR, RightBoundariesR}));
             massiveOfThresholR[j].setPresentationName("Threshold"+"radials");
            
        
            massiveReports[j] = 
            simulation_0.getReportManager().createReport(AreaAverageReport.class);
            massiveReports[j].setFieldFunction(primitiveFieldFunction_0);
            massiveReports[j].getParts().setQuery(null);
            massiveReports[j].getParts().setObjects(massiveOfThresholR[j]); 
            
            if (massiveReports[j].getValue() != 0) {
                massiveOfFieldFunction[j] = 
                simulation_0.getFieldFunctionManager().createFieldFunction();
                massiveOfFieldFunction[j].getTypeOption().setSelected(FieldFunctionTypeOption.Type.SCALAR);
                massiveOfFieldFunction[j].setPresentationName("ModifHeatTransferTem");
                massiveOfFieldFunction[j].setFunctionName("ModifHeatTransferTem");
                if (flagTemOrHTC) {
                    massiveOfFieldFunction[j].setDimensions(Dimensions.Builder().temperature(1).build());
                } else {
                    massiveOfFieldFunction[j].setDimensions(Dimensions.Builder().power(1).length(-2).temperature(-1).build());
                }


                FieldFunctionBuilder.append(String.format("(($${Position}(\"Cylindrical 1\")[0]<%f) && ($${Position}(\"Cylindrical 1\")[0]>%f) && ($${Position}(\"Cylindrical 1\")[2]<%f) &&($${Position}(\"Cylindrical 1\")[2]>%f))?%f:0", RightBoundariesR, LeftBoundarisR,RightBoundariesZ,LeftBoundarisZ, massiveReports[j].getValue()));
                massiveOfFieldFunction[j].setDefinition(FieldFunctionBuilder.toString());
                //simulation_0.println(FieldFunctionBuilder.toString());
                ListofFunctions.add(massiveOfFieldFunction[j]);
            }
            simulation_0.getPartManager().removeObjects(massiveOfThresholR[j]);
            simulation_0.getReportManager().removeObjects(massiveReports[j]);
        }
        simulation_0.getPartManager().removeObjects(massiveOfThresholZ[i]);
    }
    
       
    StringBuilder FieldBuilder = new StringBuilder();
    for (int i = 0; i < ListofFunctions.size(); i++) {
        //FieldBuilder.append(ListofFunctions.get(i).getPresentationName());
        FieldBuilder.append("${");
        FieldBuilder.append(ListofFunctions.get(i).getFunctionName());
        FieldBuilder.append("}");
        if (i != ListofFunctions.size()-1) {
            FieldBuilder.append("+");
        }
      }
    userFieldFunction_Sum.setDefinition(FieldBuilder.toString());
    
    UserFieldFunction userFieldFunction_SumAndTem = 
      simulation_0.getFieldFunctionManager().createFieldFunction();
    userFieldFunction_SumAndTem.getTypeOption().setSelected(FieldFunctionTypeOption.Type.SCALAR);
    userFieldFunction_SumAndTem.setPresentationName("ModifHeatTransferTem_itog");
    userFieldFunction_SumAndTem.setFunctionName("ModifHeatTransferTem_itog");
    
    if (flagTemOrHTC) {
        userFieldFunction_SumAndTem.setDimensions(Dimensions.Builder().temperature(1).build());
        userFieldFunction_SumAndTem.setDefinition(String.format("($${Position}[0]> %f && $${Position}[0]<%f)? (${ModifHeatTransferTem_sum}):${HeatTransferReferenceTemperatureUserYPlus}", Lmin, Lmax));
    } else {
        userFieldFunction_SumAndTem.setDimensions(Dimensions.Builder().power(1).length(-2).temperature(-1).build());
        userFieldFunction_SumAndTem.setDefinition(String.format("($${Position}[0]> %f && $${Position}[0]<%f)? (${ModifHeatTransferTem_sum}):${HeatTransferCoefficientUserYPlus}", Lmin, Lmax));
    }

    

    ImportManager importManager_0 = 
      simulation_0.getImportManager();
    String dirPath = simulation_0.getSessionDir() + File.separator;
    String NameAndPathFile;

      if (flagVallOrRK3) {
          if (flagTemOrHTC) {
              NameAndPathFile = dirPath + simulation_0.getPresentationName() + "Mod_Tem_Vall.sbd";
              importManager_0.setExportPath(NameAndPathFile);
          } else {
              NameAndPathFile = dirPath + simulation_0.getPresentationName() + "Mod_HTC_Vall.sbd";
              importManager_0.setExportPath(NameAndPathFile);
          }
      } else {
          if (flagTemOrHTC) {
              NameAndPathFile = dirPath + simulation_0.getPresentationName() + "Mod_Tem_RK3.sbd";
              importManager_0.setExportPath(NameAndPathFile);
          } else {
              NameAndPathFile = dirPath + simulation_0.getPresentationName() + "Mod_HTC_RK3.sbd";
              importManager_0.setExportPath(NameAndPathFile);
          }
      }
 
    importManager_0.setFormatType(SolutionExportFormat.Type.SBD);
    importManager_0.setExportParts(new NeoObjectVector(new Object[] {}));
    importManager_0.setExportPartSurfaces(new NeoObjectVector(new Object[] {}));

    importManager_0.setExportBoundaries(new NeoObjectVector(new Object[] {interfaceBoundary_0}));
    importManager_0.setExportRegions(new NeoObjectVector(new Object[] {}));
    importManager_0.setExportScalars(new NeoObjectVector(new Object[] {userFieldFunction_SumAndTem}));
    importManager_0.setExportVectors(new NeoObjectVector(new Object[] {}));
    importManager_0.setExportOptionAppendToFile(false);
    importManager_0.setExportOptionDataAtVerts(false);
    importManager_0.setExportOptionSolutionOnly(false);
    importManager_0.export(resolvePath(NameAndPathFile), new NeoObjectVector(new Object[] {}), new NeoObjectVector(new Object[] {interfaceBoundary_0}), new NeoObjectVector(new Object[] {}), new NeoObjectVector(new Object[] {}), new NeoObjectVector(new Object[] {userFieldFunction_SumAndTem}), NeoProperty.fromString("{\'exportFormatType\': 5, \'appendToFile\': false, \'solutionOnly\': false, \'dataAtVerts\': false}"));
  
    simulation_0.getPartManager().removeObjects(thresholdPart_3,thresholdPart_4);
    simulation_0.getReportManager().removeObjects(areaAverageReport_1,surfaceStandardDeviationReport_1, maxReport_1, minReport_0, sumReport_2);
    simulation_0.getFieldFunctionManager().removeObjects(userFieldFunction_SumAndTem);
    simulation_0.getFieldFunctionManager().removeObjects(userFieldFunction_Sum);
    
    for (UserFieldFunction ListofFunction : ListofFunctions) {
          simulation_0.getFieldFunctionManager().removeObjects(ListofFunction);
    }
  }
}
}
