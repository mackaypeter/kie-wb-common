/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.workbench.common.dmn.client.commands;

import java.util.Optional;

import org.kie.workbench.common.dmn.client.widgets.grid.model.BaseUIModelMapper;
import org.kie.workbench.common.stunner.core.client.canvas.AbstractCanvasHandler;
import org.kie.workbench.common.stunner.core.client.canvas.command.AbstractCanvasCommand;
import org.kie.workbench.common.stunner.core.client.canvas.command.AbstractCanvasGraphCommand;
import org.kie.workbench.common.stunner.core.client.command.CanvasCommandResultBuilder;
import org.kie.workbench.common.stunner.core.client.command.CanvasViolation;
import org.kie.workbench.common.stunner.core.command.Command;
import org.kie.workbench.common.stunner.core.command.CommandResult;
import org.kie.workbench.common.stunner.core.graph.command.GraphCommandExecutionContext;
import org.kie.workbench.common.stunner.core.graph.command.GraphCommandResultBuilder;
import org.kie.workbench.common.stunner.core.graph.command.impl.AbstractGraphCommand;
import org.kie.workbench.common.stunner.core.rule.RuleViolation;
import org.uberfire.ext.wires.core.grids.client.model.GridCell;
import org.uberfire.ext.wires.core.grids.client.model.GridCellValue;
import org.uberfire.ext.wires.core.grids.client.widget.grid.GridWidget;

public class SetCellValueCommand<T> extends AbstractCanvasGraphCommand implements VetoExecutionCommand,
                                                                                  VetoUndoCommand {

    private int rowIndex;
    private int columnIndex;
    private GridWidget gridWidget;
    private GridCellValue<T> cellValue;
    private BaseUIModelMapper<?> uiModelMapper;
    private Optional<GridCellValue<?>> oldCellValue;

    public SetCellValueCommand(final int rowIndex,
                               final int columnIndex,
                               final GridWidget gridWidget,
                               final GridCellValue<T> cellValue,
                               final BaseUIModelMapper<?> uiModelMapper) {
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
        this.gridWidget = gridWidget;
        this.cellValue = cellValue;
        this.uiModelMapper = uiModelMapper;
        this.oldCellValue = Optional.ofNullable(extractGridCellValue(rowIndex,
                                                                     columnIndex));
    }

    GridCellValue<?> extractGridCellValue(final int rowIndex,
                                          final int columnIndex) {
        final GridCell<?> cell = gridWidget.getModel().getCell(rowIndex,
                                                               columnIndex);
        return cell == null ? null : cell.getValue();
    }

    @Override
    protected Command<GraphCommandExecutionContext, RuleViolation> newGraphCommand(final AbstractCanvasHandler context) {
        return new AbstractGraphCommand() {
            @Override
            protected CommandResult<RuleViolation> check(final GraphCommandExecutionContext context) {
                return GraphCommandResultBuilder.SUCCESS;
            }

            @Override
            public CommandResult<RuleViolation> execute(final GraphCommandExecutionContext context) {
                uiModelMapper.toDMNModel(rowIndex,
                                         columnIndex,
                                         () -> Optional.of(cellValue));
                return GraphCommandResultBuilder.SUCCESS;
            }

            @Override
            public CommandResult<RuleViolation> undo(final GraphCommandExecutionContext context) {
                uiModelMapper.toDMNModel(rowIndex,
                                         columnIndex,
                                         () -> oldCellValue);
                return GraphCommandResultBuilder.SUCCESS;
            }
        };
    }

    @Override
    protected Command<AbstractCanvasHandler, CanvasViolation> newCanvasCommand(final AbstractCanvasHandler context) {
        return new AbstractCanvasCommand() {
            @Override
            public CommandResult<CanvasViolation> execute(final AbstractCanvasHandler context) {
                gridWidget.getModel().setCell(rowIndex,
                                              columnIndex,
                                              cellValue);
                gridWidget.getLayer().batch();
                return CanvasCommandResultBuilder.SUCCESS;
            }

            @Override
            public CommandResult<CanvasViolation> undo(final AbstractCanvasHandler context) {
                oldCellValue.ifPresent(v -> gridWidget.getModel().setCell(rowIndex,
                                                                          columnIndex,
                                                                          v));
                gridWidget.getLayer().batch();
                return CanvasCommandResultBuilder.SUCCESS;
            }
        };
    }
}
