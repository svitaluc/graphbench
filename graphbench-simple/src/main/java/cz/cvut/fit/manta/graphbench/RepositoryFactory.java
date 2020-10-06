package cz.cvut.fit.manta.graphbench;

import cz.cvut.fit.manta.graphbench.repository.ImportRepository;
import cz.cvut.fit.manta.graphbench.repository.QueryRepository;
import org.reflections.Reflections;

import java.util.Set;

public class RepositoryFactory {
    private final Reflections reflections = new Reflections("cz.cvut.fit.manta.graphbench");

    public ImportRepository createImportRepository() {
        Set<Class<? extends ImportRepository>> repositories = reflections.getSubTypesOf(ImportRepository.class);

        if (repositories.size() != 1) {
            throw new RuntimeException("There must be only one implementation. Likely programmers error.");
        }

        Class<? extends ImportRepository> importRepository = repositories.iterator().next();
        try {
            return importRepository.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {

            throw new RuntimeException(e);
        }
    }

    public QueryRepository createQueryRepository() {
        Set<Class<? extends QueryRepository>> repositories = reflections.getSubTypesOf(QueryRepository.class);

        if (repositories.size() != 1) {
            throw new RuntimeException("There must be only one implementation. Likely programmers error.");
        }

        Class<? extends QueryRepository> queryRepository = repositories.iterator().next();
        try {
            return queryRepository.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {

            throw new RuntimeException(e);
        }
    }
}
