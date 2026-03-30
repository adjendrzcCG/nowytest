package com.example.manure.support;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * TestDataBuilder provides fluent builders for creating test data payloads.
 * Defaults provide sensible values so tests only need to override what they care about.
 */
public class TestDataBuilder {

    // -------------------------------------------------------------------------
    // Storage Facility Builder
    // -------------------------------------------------------------------------

    public static class StorageFacilityBuilder {
        private String name = "Test Storage " + UUID.randomUUID().toString().substring(0, 8);
        private String type = "SLURRY";
        private int capacityM3 = 500;
        private String location = "Test Farm, North Field";
        private String constructionDate = "2020-01-01";
        private String farmId = "FARM001";

        public StorageFacilityBuilder name(String name) { this.name = name; return this; }
        public StorageFacilityBuilder type(String type) { this.type = type; return this; }
        public StorageFacilityBuilder capacityM3(int capacityM3) { this.capacityM3 = capacityM3; return this; }
        public StorageFacilityBuilder location(String location) { this.location = location; return this; }
        public StorageFacilityBuilder constructionDate(String constructionDate) { this.constructionDate = constructionDate; return this; }
        public StorageFacilityBuilder farmId(String farmId) { this.farmId = farmId; return this; }

        public Map<String, Object> build() {
            Map<String, Object> payload = new HashMap<>();
            payload.put("name", name);
            payload.put("type", type);
            payload.put("capacityM3", capacityM3);
            payload.put("location", location);
            payload.put("constructionDate", constructionDate);
            payload.put("farmId", farmId);
            return payload;
        }
    }

    // -------------------------------------------------------------------------
    // Field Builder
    // -------------------------------------------------------------------------

    public static class FieldBuilder {
        private String name = "Test Field " + UUID.randomUUID().toString().substring(0, 8);
        private double areaHectares = 10.0;
        private String soilType = "CLAY_LOAM";
        private String nitrateZone = "NVZ";
        private String cropType = "WINTER_WHEAT";
        private String farmId = "FARM001";

        public FieldBuilder name(String name) { this.name = name; return this; }
        public FieldBuilder areaHectares(double areaHectares) { this.areaHectares = areaHectares; return this; }
        public FieldBuilder soilType(String soilType) { this.soilType = soilType; return this; }
        public FieldBuilder nitrateZone(String nitrateZone) { this.nitrateZone = nitrateZone; return this; }
        public FieldBuilder cropType(String cropType) { this.cropType = cropType; return this; }
        public FieldBuilder farmId(String farmId) { this.farmId = farmId; return this; }

        public Map<String, Object> build() {
            Map<String, Object> payload = new HashMap<>();
            payload.put("name", name);
            payload.put("areaHectares", areaHectares);
            payload.put("soilType", soilType);
            payload.put("nitrateZone", nitrateZone);
            payload.put("cropType", cropType);
            payload.put("farmId", farmId);
            return payload;
        }
    }

    // -------------------------------------------------------------------------
    // Manure Application Builder
    // -------------------------------------------------------------------------

    public static class ApplicationBuilder {
        private String storageId = "SF001";
        private String fieldId = "FIELD001";
        private String applicationDate = LocalDate.now().toString();
        private double volumeM3 = 20.0;
        private String applicationMethod = "TRAILING_SHOE";
        private String cropType = "WINTER_WHEAT";

        public ApplicationBuilder storageId(String storageId) { this.storageId = storageId; return this; }
        public ApplicationBuilder fieldId(String fieldId) { this.fieldId = fieldId; return this; }
        public ApplicationBuilder applicationDate(String applicationDate) { this.applicationDate = applicationDate; return this; }
        public ApplicationBuilder volumeM3(double volumeM3) { this.volumeM3 = volumeM3; return this; }
        public ApplicationBuilder applicationMethod(String applicationMethod) { this.applicationMethod = applicationMethod; return this; }
        public ApplicationBuilder cropType(String cropType) { this.cropType = cropType; return this; }

        public Map<String, Object> build() {
            Map<String, Object> payload = new HashMap<>();
            payload.put("storageId", storageId);
            payload.put("fieldId", fieldId);
            payload.put("applicationDate", applicationDate);
            payload.put("volumeM3", volumeM3);
            payload.put("applicationMethod", applicationMethod);
            payload.put("cropType", cropType);
            return payload;
        }
    }

    // -------------------------------------------------------------------------
    // Nutrient Analysis Builder
    // -------------------------------------------------------------------------

    public static class NutrientAnalysisBuilder {
        private String storageId = "SF001";
        private String sampleDate = LocalDate.now().toString();
        private String sampleType = "SLURRY";
        private String labReference = "LAB-" + UUID.randomUUID().toString().substring(0, 8);

        public NutrientAnalysisBuilder storageId(String storageId) { this.storageId = storageId; return this; }
        public NutrientAnalysisBuilder sampleDate(String sampleDate) { this.sampleDate = sampleDate; return this; }
        public NutrientAnalysisBuilder sampleType(String sampleType) { this.sampleType = sampleType; return this; }
        public NutrientAnalysisBuilder labReference(String labReference) { this.labReference = labReference; return this; }

        public Map<String, Object> build() {
            Map<String, Object> payload = new HashMap<>();
            payload.put("storageId", storageId);
            payload.put("sampleDate", sampleDate);
            payload.put("sampleType", sampleType);
            payload.put("labReference", labReference);
            return payload;
        }
    }

    // -------------------------------------------------------------------------
    // Nutrient Analysis Results Builder
    // -------------------------------------------------------------------------

    public static class NutrientResultsBuilder {
        private double totalNitrogenGPerL = 3.2;
        private double ammoniumNGPerL = 1.8;
        private double phosphorusGPerL = 0.45;
        private double potassiumGPerL = 2.1;
        private double dryMatterPercent = 6.5;

        public NutrientResultsBuilder totalNitrogenGPerL(double v) { this.totalNitrogenGPerL = v; return this; }
        public NutrientResultsBuilder ammoniumNGPerL(double v) { this.ammoniumNGPerL = v; return this; }
        public NutrientResultsBuilder phosphorusGPerL(double v) { this.phosphorusGPerL = v; return this; }
        public NutrientResultsBuilder potassiumGPerL(double v) { this.potassiumGPerL = v; return this; }
        public NutrientResultsBuilder dryMatterPercent(double v) { this.dryMatterPercent = v; return this; }

        public Map<String, Object> build() {
            Map<String, Object> payload = new HashMap<>();
            payload.put("totalNitrogenGPerL", totalNitrogenGPerL);
            payload.put("ammoniumNGPerL", ammoniumNGPerL);
            payload.put("phosphorusGPerL", phosphorusGPerL);
            payload.put("potassiumGPerL", potassiumGPerL);
            payload.put("dryMatterPercent", dryMatterPercent);
            return payload;
        }
    }

    // -------------------------------------------------------------------------
    // Static factory methods
    // -------------------------------------------------------------------------

    public static StorageFacilityBuilder storageFacility() {
        return new StorageFacilityBuilder();
    }

    public static FieldBuilder field() {
        return new FieldBuilder();
    }

    public static ApplicationBuilder application() {
        return new ApplicationBuilder();
    }

    public static NutrientAnalysisBuilder nutrientAnalysis() {
        return new NutrientAnalysisBuilder();
    }

    public static NutrientResultsBuilder nutrientResults() {
        return new NutrientResultsBuilder();
    }
}
