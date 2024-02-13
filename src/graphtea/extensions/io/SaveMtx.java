// GraphTea Project: http://github.com/graphtheorysoftware/GraphTea
// Copyright (C) 2012 Graph Theory Software Foundation: http://GraphTheorySoftware.com
// Copyright (C) 2008 Mathematical Science Department of Sharif University of Technology
// Distributed under the terms of the GNU General Public License (GPL): http://www.gnu.org/licenses/

package graphtea.extensions.io;

import graphtea.graph.graph.GraphModel;
import graphtea.plugins.main.saveload.core.extension.GraphWriterExtension;

import java.io.File;
import java.io.IOException;

public class SaveMtx implements GraphWriterExtension {

    public String getName() {
        return "Mtx Format";
    }

    public String getExtension() {
        return "mtx";
    }

    @Override
    public void write(File file, GraphModel graph) {
        try {
            MM.saveMtxFormat(file,graph.getAdjacencyMatrix());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getDescription() {
        return "GraphTea File Format";
    }
}
