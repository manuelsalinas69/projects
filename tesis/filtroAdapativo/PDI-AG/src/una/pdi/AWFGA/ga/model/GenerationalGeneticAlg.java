//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU Lesser General Public License for more details.
//
//  You should have received a copy of the GNU Lesser General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.

package una.pdi.AWFGA.ga.model;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.uma.jmetal.algorithm.impl.AbstractGeneticAlgorithm;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.IntegerSolution;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.comparator.ObjectiveComparator;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;

import pdi.image.util.DataProvider;
import una.pdi.AWFGA.ga.util.AWFGAUtils;

/**
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
@SuppressWarnings("serial")
public class GenerationalGeneticAlg<S extends Solution<?>> extends AbstractGeneticAlgorithm<S, S> {
  private Comparator<S> comparator;
  private int maxEvaluations;
  private int evaluations;

  private SolutionListEvaluator<S> evaluator;

  /**
   * Constructor
   */
  public GenerationalGeneticAlg(Problem<S> problem, int maxEvaluations, int populationSize,
      CrossoverOperator<S> crossoverOperator, MutationOperator<S> mutationOperator,
      SelectionOperator<List<S>, S> selectionOperator, SolutionListEvaluator<S> evaluator) {
    super(problem);
    this.maxEvaluations = maxEvaluations;
    this.setMaxPopulationSize(populationSize);

    this.crossoverOperator = crossoverOperator;
    this.mutationOperator = mutationOperator;
    this.selectionOperator = selectionOperator;

    this.evaluator = evaluator;

    comparator = new ObjectiveComparator<S>(0);
  }

  @Override
	public void run() {
	  List<S> offspringPopulation;
	    List<S> matingPopulation;

	    super.setPopulation(createInitialPopulation());
	    super.setPopulation(evaluatePopulation(super.getPopulation()));
	   // System.out.println("P.Size: "+super.getPopulation().size());
	    initProgress();
	    int i=0;
	    System.out.println("Best Result "+i+": "+getResult().toString());
	    /**
	       * MODIFCACION POR PEDIDO DE J VAZQUEZ
	       * */
//	  System.out.println("Actualizando imagen base..");
//		int[][] filterResult=AWFGAUtils.applySolution((IntegerSolution)getResult());
//		DataProvider.getInstance().updateBaseImageArray(filterResult);
	      /******/
	    while (!isStoppingConditionReached()) {
	    	
	    	
	    	System.out.println("Iteracion: "+(++i));
	      matingPopulation = selection(super.getPopulation());
	      offspringPopulation = reproduction(matingPopulation);
	      offspringPopulation = evaluatePopulation(offspringPopulation);
	      super.setPopulation(replacement(super.getPopulation(), offspringPopulation));
	      updateProgress();
	      System.out.println("While.P.Size: "+super.getPopulation().size());  
	      
	      System.out.println("Best Result "+i+": "+getResult().toString());
//	      /**
//	       * MODIFCACION POR PEDIDO DE J VAZQUEZ
//	       * */
//	  System.out.println("Actualizando imagen base..");
//		filterResult=AWFGAUtils.applySolution((IntegerSolution)getResult());
//		DataProvider.getInstance().updateBaseImageArray(filterResult);
//	      /******/
	    }
	}
  
  @Override protected boolean isStoppingConditionReached() {
    return (evaluations >= maxEvaluations);
  }

  @Override protected List<S> replacement(List<S> population, List<S> offspringPopulation) {
    Collections.sort(population, comparator);
    offspringPopulation.add(population.get(0));
    offspringPopulation.add(population.get(1));
    Collections.sort(offspringPopulation, comparator) ;
    offspringPopulation.remove(offspringPopulation.size() - 1);
    offspringPopulation.remove(offspringPopulation.size() - 1);

    return offspringPopulation;
  }

  @Override protected List<S> evaluatePopulation(List<S> population) {
    population = evaluator.evaluate(population, getProblem());

    return population;
  }

  @Override public S getResult() {
    Collections.sort(getPopulation(), comparator) ;
    return getPopulation().get(0);
  }

  @Override public void initProgress() {
    evaluations = getMaxPopulationSize();
  }

  @Override public void updateProgress() {
    evaluations += getMaxPopulationSize();
  }

  @Override public String getName() {
    return "gGA" ;
  }

  @Override public String getDescription() {
    return "Generational Genetic Algorithm" ;
  }
  
  
}
