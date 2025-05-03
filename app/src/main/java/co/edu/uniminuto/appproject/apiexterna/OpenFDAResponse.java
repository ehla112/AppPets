package co.edu.uniminuto.appproject.apiexterna;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class OpenFDAResponse {

    @SerializedName("results")
    private List<Result> results;

    public List<Result> getResults() {
        return results;
    }


    public void setResults(List<Result> results) {
        this.results = results;
    }

    public static class Result {

        @SerializedName("drug")
        private List<Drug> drug;

        @SerializedName("animal")
        private Animal animal;
        @SerializedName("reaction")
        private List<Reaction> reactions;

        public List<Drug> getDrug() {
            return drug;
        }

        public void setDrug(List<Drug> drug) {
            this.drug = drug;
        }

        public Animal getAnimal() {
            return animal;
        }

        public void setAnimal(Animal animal) {
            this.animal = animal;
        }
        public List<Reaction> getReactions() {
            return reactions;
        }

        public void setReactions(List<Reaction> reactions) {
            this.reactions = reactions;
        }

        public static class Reaction {
            @SerializedName("reaction")
            private String description;

            @SerializedName("reaction_outcome")
            private String outcome;

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getOutcome() {
                return outcome;
            }

            public void setOutcome(String outcome) {
                this.outcome = outcome;
            }
        }




        public static class Drug {
            @SerializedName("brand_name")
            private String brandName;

            @SerializedName("active_ingredient")
            private String activeIngredient;

            @SerializedName("route")
            private String route;




            public String getBrandName() {
                return brandName;
            }

            public void setBrandName(String brandName) {
                this.brandName = brandName;
            }

            public String getActiveIngredient() {
                return activeIngredient;
            }

            public void setActiveIngredient(String activeIngredient) {
                this.activeIngredient = activeIngredient;
            }

            public String getRoute() {
                return route;
            }

            public void setRoute(String route) {
                this.route = route;
            }
        }

        public static class Animal {
            @SerializedName("species")
            private String species;

            public String getSpecies() {
                return species;
            }

            public void setSpecies(String species) {
                this.species = species;
            }
        }

    }
}